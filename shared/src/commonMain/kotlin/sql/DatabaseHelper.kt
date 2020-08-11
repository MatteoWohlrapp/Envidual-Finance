package sql

import co.example.envidual.finance.touchlab.db.EnvidualFinanceDatabase
import co.example.envidual.finance.touchlab.db.Stockdata
import co.touchlab.kermit.Kermit
import com.squareup.sqldelight.db.SqlDriver
import domain.data.IntradayDataContainer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class DatabaseHelper(
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

    suspend fun insertStockData(symbols: List<String>) {
        dbRef.transactionWithContext(backgroundDispatcher) {
            symbols.forEach { symbol ->
                dbRef.tableQueries.insertStock(null, symbol)
            }
        }
    }

    suspend fun selectById(id: Long): Flow<List<Stockdata>> =
        dbRef.tableQueries
            .selectById(id)
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)

    suspend fun deleteAll() {
        dbRef.transactionWithContext(backgroundDispatcher) {
            dbRef.tableQueries.deleteAll()
        }
    }
}