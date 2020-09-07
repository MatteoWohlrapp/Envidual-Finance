package remote

import domain.data.CompanyData
import domain.data.CompanyNews

interface RemoteFinanceInterface {
    suspend fun getCompanyData(ticker: String): CompanyData
    suspend fun getCompanyNews(ticker: String, from: String, to: String): List<CompanyNews>

}