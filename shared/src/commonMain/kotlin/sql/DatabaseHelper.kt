package sql

import co.example.envidual.finance.touchlab.db.EnvidualFinanceDatabase
import co.example.envidual.finance.touchlab.db.Companies
import domain.data.CompanyData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.NoCompanyFoundException

class DatabaseHelper(
    private val backgroundDispatcher: CoroutineDispatcher
) : KoinComponent {

    private val dbReference: EnvidualFinanceDatabase by inject()

    // Searches Screen
    fun selectAllSearchesAsFlow(): Flow<List<Companies>> =
        dbReference.tableQueries
            .selectAllSearches()
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)

    suspend fun insertCompany(companyData: List<CompanyData>, time: Long) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            try {
                companyData.forEach { company ->
                    dbReference.tableQueries.insertCompany(company.country!!, company.currency!!, company.finnhubIndustry!!, company.ipo!!,
                        company.logo!!, company.marketCapitalization!!, company.name!!, company.ticker!!, company.isFavourite, true, time)
                }
            }
            catch(e: NullPointerException) {
                throw NoCompanyFoundException("No company found.")
            }
        }
    }

    suspend fun selectByTicker(ticker: String) : List<Companies> =
        dbReference.tableQueries
            .selectByTicker(ticker)
            .executeAsList()

    suspend fun changeIsFavouriteForTicker(isFavourite: Boolean, ticker: String) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.tableQueries
                .changeIsFavouriteValue(isFavourite, ticker)
        }
    }

    suspend fun changeIsSearchedForTicker(isFavourite: Boolean, ticker: String) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.tableQueries
                .changeIsChangedValue(isFavourite, ticker)
        }
    }

    suspend fun changeLastSearched(time: Long, ticker: String){
        dbReference.transactionWithContext(backgroundDispatcher){
            dbReference.tableQueries
                .changeLastSearched(time, ticker)
        }
    }

    suspend fun deleteAll() {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.tableQueries
                .deleteAll()
        }
    }


    suspend fun deleteCompanyForTicker(ticker: String) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.tableQueries.
            deleteCompany(ticker)
        }
    }

    //SearchScreen
    fun selectAllFavouritesAsFlow(): Flow<List<Companies>> =
        dbReference.tableQueries
            .selectAllFavourites()
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)

    fun selectAllFavourites(): List<Companies> =
        dbReference.tableQueries
            .selectAllFavourites()
            .executeAsList()

    fun selectCompaniesToUpdate(time: Long): List<Companies> =
        dbReference.tableQueries
            .selectCompaniesToUpdate(time)
            .executeAsList()

}