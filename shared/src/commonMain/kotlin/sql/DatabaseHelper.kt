package sql

import co.example.envidual.finance.touchlab.db.EnvidualFinanceDatabase
import co.example.envidual.finance.touchlab.db.Companies
import domain.data.CompanyData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.NoCompanyFoundException

class DatabaseHelper(
    private val backgroundDispatcher: CoroutineDispatcher
) : KoinComponent {

    private val dbReference: EnvidualFinanceDatabase by inject()

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

    suspend fun changeIsFavouriteForTicker(isFavourite: Boolean, ticker: String) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.tableQueries
                .changeIsFavouriteValue(isFavourite, ticker)
        }
    }

    suspend fun changeIsSearchedForTicker(isSearched: Boolean,  ticker: String) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.tableQueries
                .changeIsSearchedValue(isSearched, ticker)
        }
    }

    suspend fun changeLastSearched(time: Long, ticker: String){
        dbReference.transactionWithContext(backgroundDispatcher){
            dbReference.tableQueries
                .changeLastSearched(time, ticker)
        }
    }

    suspend fun selectByTicker(ticker: String) : List<CompanyData> =
        dbReference.tableQueries
            .selectByTicker(ticker)
            .executeAsList()
            .mapCompaniesToCompanyData()

    fun selectAllFavourites(): List<CompanyData> =
        dbReference.tableQueries
            .selectAllFavourites()
            .executeAsList()
            .mapCompaniesToCompanyData()

    fun selectCompaniesToUpdate(time: Long): List<CompanyData> =
        dbReference.tableQueries
            .selectCompaniesToUpdate(time)
            .executeAsList()
            .mapCompaniesToCompanyData()

    //Favourites Screens
    fun selectAllFavouritesAsFlow(): Flow<List<CompanyData>> =
        dbReference.tableQueries
            .selectAllFavourites()
            .asFlow()
            .mapToList()
            .mapCompaniesToCompanyData()
            .flowOn(backgroundDispatcher)

    // Searches Screen
    fun selectAllSearchesAsFlow(): Flow<List<CompanyData>> =
        dbReference.tableQueries
            .selectAllSearches()
            .asFlow()
            .mapToList()
            .mapCompaniesToCompanyData()
            .flowOn(backgroundDispatcher)

    suspend fun deleteAll() {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.tableQueries
                .deleteAll()
        }
    }

    suspend fun deleteCompanyByTicker(ticker: String) {
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

}