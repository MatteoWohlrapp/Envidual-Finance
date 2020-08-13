package sql

import co.example.envidual.finance.touchlab.db.EnvidualFinanceDatabase
import co.example.envidual.finance.touchlab.db.Stockdata
import co.touchlab.kermit.Kermit
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class ExploreDatabaseHelper(
    sqlDriver: SqlDriver,
    private val backgroundDispatcher: CoroutineDispatcher
) {
    private val dbRef: EnvidualFinanceDatabase = EnvidualFinanceDatabase(sqlDriver)


    fun selectAllItems(): Flow<List<Stockdata>> =
        dbRef.tableQueries
            .selectAll()
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)

    suspend fun insertStockData(tickers: List<String>) {
        dbRef.transactionWithContext(backgroundDispatcher) {
            tickers.forEach { ticker ->
                dbRef.tableQueries.insertStock(null, ticker)
            }
        }
    }

    suspend fun selectById(id: Long): Flow<List<Stockdata>> =
        dbRef.tableQueries
            .selectById(id)
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)

    suspend fun selectByName(name: String): Flow<List<Stockdata>> =
        dbRef.tableQueries
            .selectByName(name)
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)

    suspend fun deleteAll() {
        dbRef.transactionWithContext(backgroundDispatcher) {
            dbRef.tableQueries.deleteAll()
        }
    }
}