package com.tai.mark.creditrating

import app.cash.turbine.test
import com.tai.mark.creditrating.data.DEFAULT_CREDIT_RATING
import com.tai.mark.creditrating.data.StubCreditRatingRepository
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CreditRatingViewModelTest {

    private val repository = StubCreditRatingRepository()
    private lateinit var viewModel: CreditRatingViewModel

    @Test
    fun `WHEN view model is created THEN showing loading state`() = runTest {
        createViewModel()
        viewModel.uiState.test {
            assertEquals(CreditRatingUiState.Loading, awaitItem())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `WHEN credit data loaded THEN show credit summary`() = runTest {
        createViewModel()
        viewModel.uiState.test {
            expectMostRecentItem() // ignore loading state

            yield()  // Allow viewmodel coroutine to run before emitting data
            repository.emitCreditRating(DEFAULT_CREDIT_RATING)

            assertEquals(CreditRatingUiState.Loaded(DEFAULT_CREDIT_RATING), awaitItem())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `WHEN credit data load fails THEN show error`() = runTest {
        repository.shouldError = true
        createViewModel()
        viewModel.uiState.test {
            assertEquals(CreditRatingUiState.Error, awaitItem())
            ensureAllEventsConsumed()
        }
    }

    private fun createViewModel() {
        viewModel = CreditRatingViewModel(
            repository = repository,
        )
    }
}