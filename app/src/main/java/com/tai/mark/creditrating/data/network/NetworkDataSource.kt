package com.tai.mark.creditrating.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface NetworkDataSource {
    suspend fun getCreditReportInfo(): CreditReportInfo
}

class NetworkDataSourceImpl(
    private val client: HttpClient,
) : NetworkDataSource {

    override suspend fun getCreditReportInfo(): CreditReportInfo {
        val response: CreditRatingResponse = client.get(CREDIT_RATING_URL).body()
        return response.creditReportInfo
    }

    private companion object {
        private const val CREDIT_RATING_URL =
            "https://android-interview.s3.eu-west-2.amazonaws.com/endpoint.json"
    }
}
