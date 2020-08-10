package remote

import domain.data.IntradayData

interface RemoteFinanceInterface {
    suspend fun getJsonFromApi(): IntradayData
}