package com.tai.mark.creditrating.data

class LocalDataSource {

    private var creditRating: CreditRating? = null

    fun setCreditRating(creditRating: CreditRating) {
        this.creditRating = creditRating
    }

    fun getRating() = creditRating

}
