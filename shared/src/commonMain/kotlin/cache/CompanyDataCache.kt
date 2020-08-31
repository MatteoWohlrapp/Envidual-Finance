package cache

import co.example.envidual.finance.touchlab.db.Companies
import domain.data.CompanyData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.koin.core.KoinComponent
import org.koin.core.inject

class CompanyDataCache(
    private val backgroundDispatcher: CoroutineDispatcher
) : CompanyDataCacheInterface, KoinComponent {

    val dbHelper: DatabaseHelper by inject()

    override suspend fun insert(companyData: List<CompanyData>) {
        dbHelper.insertCompanyData(companyData)
    }

    override suspend fun updateIsFavouriteByTicker(isFavourite: Boolean, ticker: String) {
        dbHelper.changeIsFavouriteByTickerInCompanies(isFavourite, ticker)
    }

    override suspend fun updateIsSearchedByTicker(isSearched: Boolean, ticker: String) {
        dbHelper.changeIsSearchedByTickerInCompanies(isSearched, ticker)
    }

    override suspend fun updateLastSearchedByTicker(lastSearched: Long, ticker: String) {
        dbHelper.changeLastSearchedInCompanies(lastSearched, ticker)
    }

    override fun selectByTicker(ticker: String): List<CompanyData> {
        return dbHelper.selectByTickerFromCompanies(ticker)
            .executeAsList()
            .mapCompaniesToCompanyData()
    }

    override fun selectCompaniesToUpdate(lastSearched: Long) : List<CompanyData> {
        return dbHelper.selectCompaniesToUpdateFromCompanies(lastSearched)
            .executeAsList()
            .mapCompaniesToCompanyData()
    }

    override fun selectAllFavourites(): List<CompanyData> {
        return dbHelper.selectAllFavouritesFromCompanies()
            .executeAsList()
            .mapCompaniesToCompanyData()
    }

    override fun selectAllFavouritesAsFlow(): Flow<List<CompanyData>> {
        return dbHelper.selectAllFavouritesAsFlowFromCompanies()
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)
            .mapCompaniesToCompanyData()
    }

    override fun selectAllSearchesAsFlow(): Flow<List<CompanyData>> {
        return dbHelper.selectAllSearchesAsFlowFromCompanies()
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)
            .mapCompaniesToCompanyData()
    }

    override suspend fun deleteAll() {
        dbHelper.deleteAllFromCompanies()
    }

    override suspend fun deleteByTicker(ticker: String) {
        dbHelper.deleteCompanyByTickerFromCompanies(ticker)
    }

    //    helper Functions for mapping flows of companies to company data
    fun List<Companies>.mapCompaniesToCompanyData() : List<domain.data.CompanyData>{
        val listOfCompanyData = mutableListOf<domain.data.CompanyData>()
        for (company in this)
            listOfCompanyData.add(CompanyData(company.country, company.currency, company.finnhubIndustry, company.ipo, company.logo, company.marketCapitalization, company.name, company.shareOutstanding, company.ticker, company.isFavourite, company.isSearched, company.lastSearched))
        return listOfCompanyData
    }

    fun Flow<List<Companies>>.mapCompaniesToCompanyData() : Flow<List<domain.data.CompanyData>> = map {
        val listOfCompanyData = mutableListOf<domain.data.CompanyData>()
        for (company in it)
            listOfCompanyData.add(CompanyData(company.country, company.currency, company.finnhubIndustry, company.ipo, company.logo, company.marketCapitalization, company.name, company.shareOutstanding, company.ticker, company.isFavourite, company.isSearched, company.lastSearched))
        return@map listOfCompanyData
    }
}