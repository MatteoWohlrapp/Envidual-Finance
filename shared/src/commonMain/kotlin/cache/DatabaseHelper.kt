package cache



import co.example.Companies
import co.example.CompaniesNews
import co.example.EnvidualFinanceDatabase
import co.touchlab.stately.freeze
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.TransactionWithReturn
import com.squareup.sqldelight.TransactionWithoutReturn
import domain.data.CompanyData
import domain.data.CompanyNews
import domain.use_cases.backgroundDispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.ext.scope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class DatabaseHelper(private val dbReference: EnvidualFinanceDatabase) {

    /**
     * Methods for the Companies table
     * **/

    suspend fun insertCompanyData(companyData: List<CompanyData>) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            companyData.forEach { company ->
                dbReference.companyDataTableQueries.insertCompanies(
                    company.country!!,
                    company.currency!!,
                    company.finnhubIndustry!!,
                    company.ipo!!,
                    company.logo!!,
                    company.marketCapitalization!!,
                    company.name!!,
                    company.shareOutstanding!!,
                    company.ticker!!,
                    company.isFavourite,
                    company.isSearched,
                    company.lastSearched
                )
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

    suspend fun changeLastSearchedInCompanies(time: Long, ticker: String) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.companyDataTableQueries
                .changeLastSearchedInCompanies(time, ticker)
        }
    }

    suspend fun selectByTickerFromCompanies(ticker: String): Query<Companies> =
        dbReference.transactionWithContextAndReturn(backgroundDispatcher) {
            dbReference.companyDataTableQueries.selectByTickerFromCompanies(ticker)
        }

    suspend fun selectAllSearchesFromCompanies(): Query<Companies> =
        dbReference.transactionWithContextAndReturn(backgroundDispatcher) {
            dbReference.companyDataTableQueries
                .selectAllSearchesFromCompanies()
        }

    suspend fun selectAllFavouritesFromCompanies(): Query<Companies> =
        dbReference.transactionWithContextAndReturn(backgroundDispatcher) {
            dbReference.companyDataTableQueries
                .selectAllFavouritesFromCompanies()
        }

    suspend fun selectCompaniesToUpdateFromCompanies(time: Long): Query<Companies> =
        dbReference.transactionWithContextAndReturn(backgroundDispatcher) {
            dbReference.companyDataTableQueries
                .selectCompaniesToUpdateFromCompanies(time)
        }


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
                dbReference.companyNewsTableQueries.insertCompaniesNews(
                    company.ticker!!,
                    company.category!!,
                    company.datetime!!,
                    company.headline!!,
                    company.id!!,
                    company.image!!,
                    company.related!!,
                    company.source!!,
                    company.summary!!,
                    company.url!!
                )
            }
        }
    }

    fun selectByTickerFromCompaniesNews(ticker: String): Query<CompaniesNews> =
        dbReference.companyNewsTableQueries
            .selectByTickerFromCompaniesNews(ticker)

    fun selectByTickerFromCompaniesNewsAsFlow(ticker: String): Query<CompaniesNews> =
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

    private suspend fun Transacter.transactionWithContext(
        coroutineContext: CoroutineContext,
        noEnclosing: Boolean = false,
        body: TransactionWithoutReturn.() -> Unit
) {
    withContext(coroutineContext) {
        this@transactionWithContext.transaction(noEnclosing) { body() }
    }
}

private suspend fun Transacter.transactionWithContextAndReturn(
    context: CoroutineContext,
    noEnclosing: Boolean = false,
    body: TransactionWithReturn<Query<Companies>>.() -> Query<Companies>
) : Query<Companies>{
    return withContext(context){
        transactionWithResult(noEnclosing) { body() }
    }
}
}
