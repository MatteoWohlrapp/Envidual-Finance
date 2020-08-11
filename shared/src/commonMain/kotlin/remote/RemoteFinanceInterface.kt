package remote

import domain.data.IntradayDataContainer

interface RemoteFinanceInterface {
    suspend fun getIntradayData(): IntradayDataContainer
}