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


class GetCompanyBySymbolUseCase : KoinComponent {

    private val remoteFinance : RemoteFinanceInterface by inject()
    private val dbHelper : DatabaseHelper by inject()

    suspend fun getCompany(symbol: String) : CompanyData {
        println("Before Database search")

        dbHelper.selectByName(symbol).collect {
            println(it.toString())
        }
        println("Before Database select")
        dbHelper.selectAllItems().collect{
            println(it.toString())
        }
        println("After Database select")
        val data = remoteFinance.getCompanyData(symbol)
        println(data.toString())
        return data
    }
}