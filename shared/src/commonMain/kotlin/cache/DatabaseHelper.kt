package cache

import co.example.envidual.finance.touchlab.db.EnvidualFinanceDatabase
import co.example.envidual.finance.touchlab.db.Companies
import co.example.envidual.finance.touchlab.db.CompaniesNews
import com.squareup.sqldelight.Query
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

    suspend fun insertCompanyData(companyData: List<CompanyData>) {
        dbReference.transactionWithContext(backgroundDispatcher) {
                companyData.forEach { company ->
                    dbReference.tableQueries.insertCompanies(company.country!!, company.currency!!, company.finnhubIndustry!!, company.ipo!!,
                        company.logo!!, company.marketCapitalization!!, company.name!!, company.shareOutstanding!!, company.ticker!!, company.isFavourite, company.isSearched, company.lastSearched)
                }
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

    fun selectByTickerFromCompanies(ticker: String) : Query<Companies> =
        dbReference.tableQueries
            .selectByTickerFromCompanies(ticker)

    fun selectAllFavouritesFromCompanies(): Query<Companies> =
        dbReference.tableQueries
            .selectAllFavouritesFromCompanies()

    fun selectCompaniesToUpdateFromCompanies(time: Long): Query<Companies> =
        dbReference.tableQueries
            .selectCompaniesToUpdateFromCompanies(time)

    //Favourites Screens
    fun selectAllFavouritesAsFlowFromCompanies():Query<Companies> =
        dbReference.tableQueries
            .selectAllFavouritesFromCompanies()


    // Searches Screen
    fun selectAllSearchesAsFlowFromCompanies(): Query<Companies> =
        dbReference.tableQueries
            .selectAllSearchesFromCompanies()


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


    /**
     * Methods for the CompaniesNews table
     * **/

    suspend fun insertCompaniesNews(companiesNews: List<CompanyNews>) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            companiesNews.forEach { company ->
                dbReference.tableQueries.insertCompaniesNews(company.ticker!!, company.category!!, company.datetime!!, company.headline!!, company.id!!, company.image!!, company.related!!, company.source!!, company.summary!!, company.url!!)
            }
        }
    }

    fun selectByTickerFromCompaniesNews(ticker: String) : Query<CompaniesNews> =
        dbReference.tableQueries
            .selectByTickerFromCompaniesNews(ticker)

    fun selectByTickerFromCompaniesNewsAsFlow(ticker:String): Query<CompaniesNews> =
        dbReference.tableQueries
            .selectByTickerFromCompaniesNews(ticker)

    suspend fun deleteAllFromCompanyNews() {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.tableQueries
                .deleteAllFromCompaniesNews()
        }
    }

    suspend fun deleteCompanyNewsByTicker(ticker: String) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.tableQueries.
            deleteCompanyNewsWithTicker(ticker)
        }
    }

    suspend fun deleteCompanyNewsByTickerAndTimestamp(ticker: String, dateTime: Long) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.tableQueries.
            deleteCompanyNewsWithTickerAndTimestamp(ticker, dateTime)
        }
    }
}