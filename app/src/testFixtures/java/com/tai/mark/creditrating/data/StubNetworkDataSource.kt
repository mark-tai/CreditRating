package com.tai.mark.creditrating.data

import com.tai.mark.creditrating.data.network.CreditReportInfo
import com.tai.mark.creditrating.data.network.NetworkDataSource


class StubNetworkDataSource : NetworkDataSource {
    var creditRatingInfo: CreditReportInfo? = null

    override suspend fun getCreditReportInfo(): CreditReportInfo =
        creditRatingInfo ?: throw IllegalArgumentException()
}
