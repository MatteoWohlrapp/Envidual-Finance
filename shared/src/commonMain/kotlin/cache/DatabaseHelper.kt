package cache

import co.example.envidual.finance.touchlab.db.EnvidualFinanceDatabase
import co.example.envidual.finance.touchlab.db.Companies
import co.example.envidual.finance.touchlab.db.CompaniesNews
import co.touchlab.stately.freeze
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
                    dbReference.companyDataTableQueries.insertCompanies(company.country!!, company.currency!!, company.finnhubIndustry!!, company.ipo!!,
                        company.logo!!, company.marketCapitalization!!, company.name!!, company.shareOutstanding!!, company.ticker!!, company.isFavourite, company.isSearched, company.lastSearched)
                }
        }
    }

    suspend fun changeIsFavouriteByTickerInCompanies(isFavourite: Boolean, ticker: String) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.companyDataTableQueries
                .changeIsFavouriteValueInCompanies(isFavourite, ticker)
        }
    }

    suspend fun changeIsSearchedByTickerInCompanies(isSearched: Boolean, ticker: String) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.companyDataTableQueries
                .changeIsSearchedValueInCompanies(isSearched, ticker)
        }
    }

    suspend fun changeLastSearchedInCompanies(time: Long, ticker: String){
        dbReference.transactionWithContext(backgroundDispatcher){
            dbReference.companyDataTableQueries
                .changeLastSearchedInCompanies(time, ticker)
        }
    }

    fun selectByTickerFromCompanies(ticker: String) : Query<Companies> =
        dbReference.companyDataTableQueries
            .selectByTickerFromCompanies(ticker)

    fun selectAllFavouritesFromCompanies(): Query<Companies> =
        dbReference.companyDataTableQueries
            .selectAllFavouritesFromCompanies()

    fun selectCompaniesToUpdateFromCompanies(time: Long): Query<Companies> =
        dbReference.companyDataTableQueries
            .selectCompaniesToUpdateFromCompanies(time)

    //Favourites Screens
    fun selectAllFavouritesAsFlowFromCompanies():Query<Companies> =
        dbReference.companyDataTableQueries
            .selectAllFavouritesFromCompanies()


    // Searches Screen
    fun selectAllSearchesAsFlowFromCompanies(): Query<Companies> =
        dbReference.companyDataTableQueries
            .selectAllSearchesFromCompanies()


    suspend fun deleteAllFromCompanies() {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.companyDataTableQueries
                .deleteAllFromCompanies()
        }
    }

    suspend fun deleteCompanyByTickerFromCompanies(ticker: String) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.companyDataTableQueries
                .deleteCompany(ticker)
        }
    }


    /**
     * Methods for the CompaniesNews table
     * **/

    suspend fun insertCompaniesNews(companiesNews: List<CompanyNews>) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            companiesNews.forEach { company ->
                dbReference.companyNewsTableQueries.insertCompaniesNews(company.ticker!!, company.category!!, company.datetime!!, company.headline!!, company.id!!, company.image!!, company.related!!, company.source!!, company.summary!!, company.url!!)
            }
        }
    }

    fun selectByTickerFromCompaniesNews(ticker: String) : Query<CompaniesNews> =
        dbReference.companyNewsTableQueries
            .selectByTickerFromCompaniesNews(ticker)

    fun selectByTickerFromCompaniesNewsAsFlow(ticker:String): Query<CompaniesNews> =
        dbReference.companyNewsTableQueries
            .selectByTickerFromCompaniesNews(ticker)

    suspend fun deleteAllFromCompanyNews() {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.companyNewsTableQueries
                .deleteAllFromCompaniesNews()
        }
    }

    suspend fun deleteCompanyNewsByTicker(ticker: String) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.companyNewsTableQueries
                .deleteCompanyNewsWithTicker(ticker)
        }
    }

    suspend fun deleteCompanyNewsByTickerAndTimestamp(ticker: String, dateTime: Long) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.companyNewsTableQueries
                .deleteCompanyNewsWithTickerAndTimestamp(ticker, dateTime)
        }
    }
}