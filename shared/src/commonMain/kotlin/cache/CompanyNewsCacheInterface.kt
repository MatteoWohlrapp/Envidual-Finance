package cache

import co.example.envidual.finance.touchlab.db.CompaniesNews
import domain.data.CompanyNews
import kotlinx.coroutines.flow.Flow

interface CompanyNewsCacheInterface {
    suspend fun insert(companiesNews: List<CompanyNews>)
    fun selectByTicker(ticker:String) : List<CompanyNews>
    fun selectByTickerAsFlow(ticker: String) : Flow<List<CompanyNews>>
    suspend fun deleteAll()
    suspend fun deleteByTicker(ticker: String)
    suspend fun deleteByTickerAndDateTime(ticker: String, dateTime: Long)
}