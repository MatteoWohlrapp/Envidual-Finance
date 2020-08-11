package remote

import domain.data.CompanyData

interface RemoteFinanceInterface {
    suspend fun getCompanyData(symbol: String): CompanyData
}