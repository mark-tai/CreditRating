package com.tai.mark.creditrating.data.network

import kotlinx.serialization.Serializable

@Serializable
data class CreditRatingResponse(
    val creditReportInfo: CreditReportInfo
)

@Serializable
data class CreditReportInfo(
    val score: Int,
    val maxScoreValue: Int,
)
