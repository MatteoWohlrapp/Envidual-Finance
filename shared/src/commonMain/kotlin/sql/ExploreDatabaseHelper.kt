package sql

import co.example.envidual.finance.touchlab.db.EnvidualFinanceDatabase
import co.example.envidual.finance.touchlab.db.Explorecompanydata
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class ExploreDatabaseHelper(
    sqlDriver: SqlDriver,
    private val backgroundDispatcher: CoroutineDispatcher
) {
    private val dbReference = EnvidualFinanceDatabase(sqlDriver)


    fun selectAllItems(): Flow<List<Explorecompanydata>> =
        dbReference.tableQueries
            .selectAll()
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)

    suspend fun insertExploreCompanyData(explorecompanydata: List<Explorecompanydata>) {
        dbReference.transactionWithContext(backgroundDispatcher) {
            explorecompanydata.forEach { explorecompanydata ->
                dbReference.tableQueries.insertStock(null, explorecompanydata.country, explorecompanydata.finnhubIndustry, explorecompanydata.ipo,
                explorecompanydata.logo, explorecompanydata.marketCapitalization, explorecompanydata.name, explorecompanydata.ticker)
            }
        }
    }

    suspend fun selectById(id: Long): Flow<List<Explorecompanydata>> =
        dbReference.tableQueries
            .selectById(id)
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)

    suspend fun selectByTicker(name: String): Flow<List<Explorecompanydata>> =
        dbReference.tableQueries
            .selectByTicker(name)
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)

    suspend fun selectByCategory(name: String): Flow<List<Explorecompanydata>> =
        dbReference.tableQueries
            .selectByIndustry(name)
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)


    suspend fun deleteAll() {
        dbReference.transactionWithContext(backgroundDispatcher) {
            dbReference.tableQueries.deleteAll()
        }
    }
}