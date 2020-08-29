package remote

import domain.data.CompanyData
import domain.data.CompanyNews

interface RemoteFinanceInterface {
    suspend fun getCompanyData(ticker: String): CompanyData

    suspend fun getCompanyNews(ticker: String): List<CompanyNews>
//suspend fun getCompanyNews(ticker: String): String

}