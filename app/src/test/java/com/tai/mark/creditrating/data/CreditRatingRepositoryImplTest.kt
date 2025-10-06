package com.tai.mark.creditrating.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class) // Required for Dispatchers.setMain/resetMain
class CreditRatingRepositoryImplTest {

    private val localDataSource = LocalDataSource()
    private val networkDataSource = StubNetworkDataSource()

    private val dispatcher = StandardTestDispatcher()
    private val repository = CreditRatingRepositoryImpl(
        localDataSource = localDataSource,
        networkDataSource = networkDataSource,
        dispatcher = dispatcher,
    )

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterEach
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN rating is cached WHEN load rating THEN return cached rating`() = runTest {
        localDataSource.setCreditRating(DEFAULT_CREDIT_RATING)

        assertEquals(DEFAULT_CREDIT_RATING, repository.loadCreditRating())
    }

    @Test
    fun `GIVEN no cached rating WHEN load rating THEN load from network source`() = runTest {
        networkDataSource.creditRatingInfo = DEFAULT_CREDIT_RATING_INFO

        assertEquals(DEFAULT_CACHED_CREDIT_RATING, repository.loadCreditRating())
    }

    @Test
    fun `WHEN rating loaded from network THEN cached rating updated`() = runTest {
        networkDataSource.creditRatingInfo = DEFAULT_CREDIT_RATING_INFO
        repository.loadCreditRating()

        assertEquals(
            CreditRating(
                score = NETWORK_RATING_SCORE,
                maxScore = NETWORK_RATING_MAX_SCORE,
            ),
            localDataSource.getRating()
        )
    }
}