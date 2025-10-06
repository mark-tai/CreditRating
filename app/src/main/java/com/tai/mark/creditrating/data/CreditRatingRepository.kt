package com.tai.mark.creditrating.data

import com.tai.mark.creditrating.data.network.CreditReportInfo
import com.tai.mark.creditrating.data.network.NetworkDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

interface CreditRatingRepository {

    suspend fun loadCreditRating(): CreditRating
}

class CreditRatingRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val networkDataSource: NetworkDataSource,
    private val coroutineScope: CoroutineScope,
) : CreditRatingRepository {

    override suspend fun loadCreditRating(): CreditRating =
        withContext(coroutineScope.coroutineContext) {
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
