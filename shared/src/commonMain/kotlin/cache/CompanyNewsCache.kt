package cache

import co.example.envidual.finance.touchlab.db.CompaniesNews
import domain.data.CompanyNews
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.koin.core.KoinComponent
import org.koin.core.inject

class CompanyNewsCache(
    private val backgroundDispatcher: CoroutineDispatcher
) : CompanyNewsCacheInterface, KoinComponent{

    val dbHelper: DatabaseHelper by inject()

    override suspend fun insert(companiesNews: List<CompanyNews>) {
        dbHelper.insertCompaniesNews(companiesNews)
    }

    override fun selectByTicker(ticker: String): List<CompanyNews> {
        return dbHelper.selectByTickerFromCompaniesNews(ticker)
            .executeAsList()
            .mapCompaniesNewsToCompanyNews()
    }

    override fun selectByTickerAsFlow(ticker: String): Flow<List<CompanyNews>> {
        return dbHelper.selectByTickerFromCompaniesNewsAsFlow(ticker)
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)
            .mapCompaniesNewsToCompanyNews()
    }

    override suspend fun deleteAll() {
        dbHelper.deleteAllFromCompanyNews()
    }

    override suspend fun deleteByTicker(ticker: String) {
        dbHelper.deleteCompanyNewsByTicker(ticker)
    }

    override suspend fun deleteByTickerAndDateTime(ticker: String, dateTime: Long) {
        dbHelper.deleteCompanyNewsByTickerAndTimestamp(ticker, dateTime)
    }

    //    helper Functions for mapping flows of companiesNews to companyNews
    fun List<CompaniesNews>.mapCompaniesNewsToCompanyNews() : List<domain.data.CompanyNews>{
        val listOfCompanyData = mutableListOf<domain.data.CompanyNews>()
        for (company in this)
            listOfCompanyData.add(CompanyNews(company.ticker, company.category, company.datetime, company.headline, company.id, company.image, company.related, company.source, company.summary, company.url))
        return listOfCompanyData
    }

    fun Flow<List<CompaniesNews>>.mapCompaniesNewsToCompanyNews() : Flow<List<domain.data.CompanyNews>> = map {
        val listOfCompanyData = mutableListOf<domain.data.CompanyNews>()
        for (company in it)
            listOfCompanyData.add(CompanyNews(company.ticker, company.category, company.datetime, company.headline, company.id, company.image, company.related, company.source, company.summary, company.url))
        return@map listOfCompanyData
    }
}