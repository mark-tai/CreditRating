package com.tai.mark.creditrating.data

import kotlinx.serialization.Serializable

@Serializable
data class CreditRating(
    val score: Int,
    val maxScore: Int,
)