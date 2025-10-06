package com.tai.mark.creditrating.data

import com.tai.mark.creditrating.data.network.CreditReportInfo
import com.tai.mark.creditrating.data.network.NetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface CreditRatingRepository {

    suspend fun loadCreditRating(): CreditRating
}

class CreditRatingRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val networkDataSource: NetworkDataSource,
    private val dispatcher: CoroutineDispatcher,
) : CreditRatingRepository {

    override suspend fun loadCreditRating(): CreditRating =
        withContext(dispatcher) {
            localDataSource.getRating() ?: loadFromNetwork()
        }

    private suspend fun loadFromNetwork(): CreditRating {
        val rating = networkDataSource.getCreditReportInfo().toCreditRating()
        localDataSource.setCreditRating(rating)
        return rating
    }

    private fun CreditReportInfo.toCreditRating(): CreditRating =
        CreditRating(
            score = score,
            maxScore = maxScoreValue,
        )
}
