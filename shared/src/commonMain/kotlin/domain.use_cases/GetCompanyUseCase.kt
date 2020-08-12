package domain.use_cases

import com.squareup.sqldelight.db.SqlDriver
import domain.data.CompanyData
import io.ktor.http.ContentType
import kotlinx.coroutines.flow.collect
import remote.FinanceRemote
import remote.RemoteFinanceInterface
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import sql.DatabaseHelper


class GetCompanyUseCase : KoinComponent {

    private val remoteFinance : RemoteFinanceInterface by inject()
    private val dbHelper : DatabaseHelper by inject()

    suspend fun getCompany(symbol: String) : CompanyData{
        dbHelper.insertStockData(listOf("APL"))
        dbHelper.selectAllItems().collect{
            println(it.toString())
        }
        return remoteFinance.getCompanyData(symbol)
    }
}