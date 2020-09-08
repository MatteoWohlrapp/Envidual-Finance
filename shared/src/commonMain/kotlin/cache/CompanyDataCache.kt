package cache

import co.example.envidual.finance.touchlab.db.Companies
import co.touchlab.stately.freeze
import co.touchlab.stately.isFrozen
import domain.data.CompanyData
import domain.use_cases.backgroundDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.native.concurrent.ThreadLocal

class CompanyDataCache(
    private  val dbHelper: DatabaseHelper
) : CompanyDataCacheInterface {

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

    override suspend fun selectByTicker(ticker: String): List<CompanyData> {
        return dbHelper.selectByTickerFromCompanies(ticker)
            .executeAsList()
            .mapCompaniesToCompanyData()
            .freeze()
    }

    override suspend fun selectCompaniesToUpdate(lastSearched: Long): List<CompanyData> {
        return dbHelper.selectCompaniesToUpdateFromCompanies(lastSearched)
            .executeAsList()
            .mapCompaniesToCompanyData()
            .freeze()
    }

    override suspend fun selectAllFavourites(): List<CompanyData> {
        return dbHelper.selectAllFavouritesFromCompanies()
            .executeAsList()
            .mapCompaniesToCompanyData()
            .freeze()
    }

    override suspend fun selectAllFavouritesAsFlow(): Flow<List<CompanyData>> {
        return dbHelper.selectAllFavouritesFromCompanies()
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)
            .mapCompaniesToCompanyData()
            .freeze()
    }

    override suspend fun selectAllSearchesAsFlow(): Flow<List<CompanyData>> {
        return dbHelper.selectAllSearchesFromCompanies()
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)
            .mapCompaniesToCompanyData()
            .freeze()
    }

    override suspend fun deleteAll() {
        dbHelper.deleteAllFromCompanies()
    }

    override suspend fun deleteByTicker(ticker: String) {
        dbHelper.deleteCompanyByTickerFromCompanies(ticker)
    }

    //    helper Functions for mapping flows of companies to company data
    fun List<Companies>.mapCompaniesToCompanyData(): List<domain.data.CompanyData> {
        val listOfCompanyData = mutableListOf<domain.data.CompanyData>()
        for (company in this)
            listOfCompanyData.add(
                CompanyData(
                    company.country,
                    company.currency,
                    company.finnhubIndustry,
                    company.ipo,
                    company.logo,
                    company.marketCapitalization,
                    company.name,
                    company.shareOutstanding,
                    company.ticker,
                    company.isFavourite,
                    company.isSearched,
                    company.lastSearched
                )
            )
        return listOfCompanyData
    }

    fun Flow<List<Companies>>.mapCompaniesToCompanyData(): Flow<List<domain.data.CompanyData>> =
        map {
            val listOfCompanyData = mutableListOf<domain.data.CompanyData>()
            for (company in it)
                listOfCompanyData.add(
                    CompanyData(
                        company.country,
                        company.currency,
                        company.finnhubIndustry,
                        company.ipo,
                        company.logo,
                        company.marketCapitalization,
                        company.name,
                        company.shareOutstanding,
                        company.ticker,
                        company.isFavourite,
                        company.isSearched,
                        company.lastSearched
                    )
                )
            return@map listOfCompanyData
        }
}

expect fun getThread(): String