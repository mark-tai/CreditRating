package com.tai.mark.creditrating.data

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import java.io.IOException

class StubCreditRatingRepository : CreditRatingRepository {


    // Use share flows with 0 replay and 0 buffer to allow simple mocking of the repository
    private var creditRatingFlow = MutableSharedFlow<CreditRating>(
        replay = 0,
        extraBufferCapacity = 0,
    )

    var shouldError: Boolean = false

    suspend fun emitCreditRating(creditRating: CreditRating) {
        creditRatingFlow.emit(creditRating)
    }

    override suspend fun loadCreditRating(): CreditRating =
        if (!shouldError) {
            creditRatingFlow.first()
        } else {
            throw IOException()
        }
}