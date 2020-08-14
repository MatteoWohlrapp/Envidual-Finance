package sql

import co.example.envidual.finance.touchlab.db.EnvidualFinanceDatabase
import co.example.envidual.finance.touchlab.db.Favourites
import com.squareup.sqldelight.db.SqlDriver
import domain.data.CompanyData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.KoinComponent
import org.koin.core.inject

class FavouritesDatabaseHelper(
    private val backgroundDispatcher: CoroutineDispatcher
): KoinComponent {


    private val dbReference : EnvidualFinanceDatabase by inject()


    fun selectAllItemsFromFavouritesAsFlow(): Flow<List<Favourites>> =
        dbReference.tableQueries
            .selectAllFromFavourites()
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)

    fun selectAllItemsFromFavourites():List<Favourites> =
        dbReference.tableQueries
            .selectAllFromFavourites()
            .executeAsList()

    suspend fun insertFavourites(companiesData: List<CompanyData>) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            companiesData.forEach { company ->
                dbReference.tableQueries.insertFavouriteCompanyFromFavourites(null, company.country!!,
                    company.currency!!, company.finnhubIndustry!!, company.ipo!!,
                    company.logo!!, company.marketCapitalization!!, company.name!!, company.ticker!!)
            }
        }
    }

    suspend fun selectByIdFromFavouritesAsFlow(id: Long): Flow<List<Favourites>> =
        dbReference.tableQueries
            .selectByIdFromFavourites(id)
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)

    suspend fun selectByTickerFromFavouritesAsFlow(name: String): Flow<List<Favourites>> =
        dbReference.tableQueries
            .selectByTickerFromFavourites(name)
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)

    suspend fun selectByTickerFromFavourites(ticker: String) : List<Favourites> =
        dbReference.tableQueries
            .selectByTickerFromFavourites(ticker)
            .executeAsList()


    suspend fun selectByCategoryFromFavouritesAsFlow(name: String): Flow<List<Favourites>> =
        dbReference.tableQueries
            .selectByIndustryFromFavourites(name)
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)


    suspend fun deleteAllFromFavourites() {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.tableQueries.deleteAllFromFavourites()
        }
    }
}