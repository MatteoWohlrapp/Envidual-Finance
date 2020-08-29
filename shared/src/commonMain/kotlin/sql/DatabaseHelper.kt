package sql

import co.example.envidual.finance.touchlab.db.EnvidualFinanceDatabase
import co.example.envidual.finance.touchlab.db.Companies
import co.example.envidual.finance.touchlab.db.CompaniesNews
import domain.data.CompanyData
import domain.data.CompanyNews
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.koin.core.KoinComponent
import org.koin.core.inject

class DatabaseHelper(
    private val backgroundDispatcher: CoroutineDispatcher
) : KoinComponent {

    private val dbReference: EnvidualFinanceDatabase by inject()



    /**
     * Methods for the Companies table
     * **/

    suspend fun insertCompanies(companyData: List<CompanyData>) {
        dbReference.transactionWithContext(backgroundDispatcher) {
//            try {
                companyData.forEach { company ->
                    dbReference.tableQueries.insertCompanies(company.country!!, company.currency!!, company.finnhubIndustry!!, company.ipo!!,
                        company.logo!!, company.marketCapitalization!!, company.name!!, company.ticker!!, company.isFavourite, company.isSearched, company.lastSearched)
                }
//            }
//            catch(e: NullPointerException) {
//                throw NoCompanyFoundException("No company found.")
//            }
        }
    }

    suspend fun changeIsFavouriteByTickerInCompanies(isFavourite: Boolean, ticker: String) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.tableQueries
                .changeIsFavouriteValueInCompanies(isFavourite, ticker)
        }
    }

    suspend fun changeIsSearchedByTickerInCompanies(isSearched: Boolean, ticker: String) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.tableQueries
                .changeIsSearchedValueInCompanies(isSearched, ticker)
        }
    }

    suspend fun changeLastSearchedInCompanies(time: Long, ticker: String){
        dbReference.transactionWithContext(backgroundDispatcher){
            dbReference.tableQueries
                .changeLastSearchedInCompanies(time, ticker)
        }
    }

    fun selectByTickerFromCompanies(ticker: String) : List<CompanyData> =
        dbReference.tableQueries
            .selectByTickerFromCompanies(ticker)
            .executeAsList()
            .mapCompaniesToCompanyData()

    fun selectAllFavouritesFromCompanies(): List<CompanyData> =
        dbReference.tableQueries
            .selectAllFavouritesFromCompanies()
            .executeAsList()
            .mapCompaniesToCompanyData()

    fun selectCompaniesToUpdateFromCompanies(time: Long): List<CompanyData> =
        dbReference.tableQueries
            .selectCompaniesToUpdateFromCompanies(time)
            .executeAsList()
            .mapCompaniesToCompanyData()

    //Favourites Screens
    fun selectAllFavouritesAsFlowFromCompanies(): Flow<List<CompanyData>> =
        dbReference.tableQueries
            .selectAllFavouritesFromCompanies()
            .asFlow()
            .mapToList()
            .mapCompaniesToCompanyData()
            .flowOn(backgroundDispatcher)

    // Searches Screen
    fun selectAllSearchesAsFlowFromCompanies(): Flow<List<CompanyData>> =
        dbReference.tableQueries
            .selectAllSearchesFromCompanies()
            .asFlow()
            .mapToList()
            .mapCompaniesToCompanyData()
            .flowOn(backgroundDispatcher)

    suspend fun deleteAllFromCompanies() {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.tableQueries
                .deleteAllFromCompanies()
        }
    }

    suspend fun deleteCompanyByTickerFromCompanies(ticker: String) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.tableQueries.
            deleteCompany(ticker)
        }
    }


//    helper Functions for mapping flows of companies to company data
    fun List<Companies>.mapCompaniesToCompanyData() : List<domain.data.CompanyData>{
        val listOfCompanyData = mutableListOf<domain.data.CompanyData>()
        for (company in this)
            listOfCompanyData.add(CompanyData(company.country, company.currency, company.finnhubIndustry, company.ipo, company.logo, company.marketCapitalization, company.name, company.ticker, company.isFavourite, company.isSearched, company.lastSearched))
        return listOfCompanyData
    }

    fun Flow<List<Companies>>.mapCompaniesToCompanyData() : Flow<List<domain.data.CompanyData>> = map {
        val listOfCompanyData = mutableListOf<domain.data.CompanyData>()
        for (company in it)
            listOfCompanyData.add(CompanyData(company.country, company.currency, company.finnhubIndustry, company.ipo, company.logo, company.marketCapitalization, company.name, company.ticker, company.isFavourite, company.isSearched, company.lastSearched))
        return@map listOfCompanyData
    }

    /**
     * Methods for the CompaniesNews table
     * **/

    suspend fun insertCompaniesNews(companiesNews: List<CompanyNews>) {
        dbReference.transactionWithContext(backgroundDispatcher) {
//            try {
            companiesNews.forEach { company ->
                dbReference.tableQueries.insertCompaniesNews(company.ticker!!, company.category!!, company.datetime!!, company.headline!!, company.id!!, company.image!!, company.related!!, company.source!!, company.summary!!, company.url!!)
            }
//            }
//            catch(e: NullPointerException) {
//                throw NoCompanyFoundException("No company found.")
//            }
        }
    }

    fun selectByTickerFromCompaniesNews(ticker: String) : List<CompanyNews> =
        dbReference.tableQueries
            .selectByTickerFromCompaniesNews(ticker)
            .executeAsList()
            .mapCompaniesNewsToCompanyNews()

    fun selectByTickerFromCompaniesNewsAsFlow(ticker:String): Flow<List<CompanyNews>> =
        dbReference.tableQueries
            .selectByTickerFromCompaniesNews(ticker)
            .asFlow()
            .mapToList()
            .mapCompaniesNewsToCompanyNews()
            .flowOn(backgroundDispatcher)

    // News Screen
    fun selectAllCompaniesNewsAsFlow(): Flow<List<CompanyNews>> =
        dbReference.tableQueries
            .selectAllFromCompaniesNews()
            .asFlow()
            .mapToList()
            .mapCompaniesNewsToCompanyNews()
            .flowOn(backgroundDispatcher)

    fun selectAllFromCompaniesNews(): List<CompanyNews> =
        dbReference.tableQueries
            .selectAllFromCompaniesNews()
            .executeAsList()
            .mapCompaniesNewsToCompanyNews()

    suspend fun deleteAllFromCompanyNews() {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.tableQueries
                .deleteAllFromCompaniesNews()
        }
    }

    suspend fun deleteCompanyNewsByTicker(ticker: String) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.tableQueries.
            deleteCompanyNews(ticker)
        }
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