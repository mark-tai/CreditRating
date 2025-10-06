package com.tai.mark.creditrating.data

import com.tai.mark.creditrating.data.network.CreditReportInfo

val DEFAULT_CREDIT_RATING =
    CreditRating(
        score = 300,
        maxScore = 700,
    )

const val NETWORK_RATING_SCORE = 100
const val NETWORK_RATING_MAX_SCORE = 700

val DEFAULT_CACHED_CREDIT_RATING =
    CreditRating(
        score = NETWORK_RATING_SCORE,
        maxScore = NETWORK_RATING_MAX_SCORE,
    )

val DEFAULT_CREDIT_RATING_INFO =
    CreditReportInfo(
        score = NETWORK_RATING_SCORE,
        maxScoreValue = NETWORK_RATING_MAX_SCORE,
    )