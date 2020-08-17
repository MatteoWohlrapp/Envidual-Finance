package sql

import co.example.envidual.finance.touchlab.db.EnvidualFinanceDatabase
import co.example.envidual.finance.touchlab.db.Favourites
import co.example.envidual.finance.touchlab.db.Searches
import com.squareup.sqldelight.db.SqlDriver
import domain.data.CompanyData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.KoinComponent
import org.koin.core.inject

class SearchesDatabaseHelper(
    private val backgroundDispatcher: CoroutineDispatcher
) : KoinComponent {


    private val dbReference: EnvidualFinanceDatabase by inject()


    fun selectAllItemsFromSearchAsFlow(): Flow<List<Searches>> =
        dbReference.tableQueries
            .selectAllFromSearch()
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)

    suspend fun insertSearches(companyData: List<CompanyData>) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            companyData.forEach { company ->
                dbReference.tableQueries.insertSearchedCompany(company.country!!, company.currency!!, company.finnhubIndustry!!, company.ipo!!,
                company.logo!!, company.marketCapitalization!!, company.name!!, company.ticker!!, company.checked)
            }
        }
    }

//    suspend fun selectByIdFromSearches(id: Long): Flow<List<Searches>> =
//        dbReference.tableQueries
//            .selectByIdFromSearch(id)
//            .asFlow()
//            .mapToList()
//            .flowOn(backgroundDispatcher)

    suspend fun selectByTickerFromSearchesAsFlow(name: String): Flow<List<Searches>> =
        dbReference.tableQueries
            .selectByTickerFromSearch(name)
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)

    suspend fun selectByTickerFromSearches(ticker: String) : List<Searches> =
        dbReference.tableQueries
            .selectByTickerFromSearch(ticker)
            .executeAsList()


    suspend fun selectByCategoryFromSearchAsFlow(name: String): Flow<List<Searches>> =
        dbReference.tableQueries
            .selectByIndustryFromSearch(name)
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)

    suspend fun changeCheckedForName(checked: Boolean, companyName:String) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.tableQueries.changeCheckedValue(checked, companyName)
        }
    }


    suspend fun deleteAllFromSearch() {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.tableQueries.deleteAllFromSearch()
        }
    }

    suspend fun deleteCompanyFromSearch(companyName: String) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.tableQueries.deleteCompanyFromSearches(companyName)
        }
    }
}