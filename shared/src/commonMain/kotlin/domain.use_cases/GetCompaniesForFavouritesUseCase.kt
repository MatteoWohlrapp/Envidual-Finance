package domain.use_cases

import domain.data.CompanyData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.ext.scope
import remote.NoCompanyFoundException
import remote.RemoteFinanceInterface
import sql.DatabaseHelper
import kotlin.native.concurrent.ThreadLocal

class GetCompaniesForFavouritesUseCase : KoinComponent {

    private val dbHelper: DatabaseHelper by inject()
    private val remoteFinance: RemoteFinanceInterface by inject()

    private val defaultFavouriteCompaniesTicker =
        mutableListOf("MSFT", "AAPL", "AMZN", "FB", "GOOGL", "IBM", "BRK.B")


    suspend fun invoke(): Flow<List<CompanyData>> {
        println("Invoke in favouritesUseCase called")
        val data = dbHelper.selectAllFavourites()
        if(data.isEmpty()){
            val exploreList = mutableListOf<CompanyData>()
            for (ticker in defaultFavouriteCompaniesTicker) {
                var data = CompanyData()
                try {
                    println("trying to access remote and searching ticker: $ticker")
                    data = remoteFinance.getCompanyData(ticker)
                    println("data successfully retrieved, name is ${data.name}")
                } catch(e: NoCompanyFoundException){
                    println(e.toString())
                    }
                if (data.name != null) {
                    data.isFavourite = true
                    exploreList.add(data)
                }
            }

            val time = getTimestamp()
            println("Timestamp is $time")
            dbHelper.insertCompany(exploreList, time)
        }

        return dbHelper.selectAllFavouritesAsFlow().map { it ->
            val companies = mutableListOf<CompanyData>()

            for(company in it){
                companies.add(CompanyData(company.country, company.currency, company.finnhubIndustry, company.ipo,
                    company.logo, company.marketCapitalization, company.name, company.ticker, company.isFavourite))
            }

            companies
        }
    }
}

expect fun getTimestamp(): Long