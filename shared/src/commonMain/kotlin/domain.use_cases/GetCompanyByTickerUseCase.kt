package domain.use_cases

import domain.data.CompanyData
import remote.RemoteFinanceInterface
import org.koin.core.KoinComponent
import org.koin.core.inject
import sql.ExploreDatabaseHelper


class GetCompanyByTickerUseCase : KoinComponent {

    private val remoteFinance : RemoteFinanceInterface by inject()
    private val dbHelperExplore : ExploreDatabaseHelper by inject()

    suspend fun getCompany(ticker: String) : CompanyData {
        println("Before Database search")


        println("After Database select")
        val data = remoteFinance.getCompanyData(ticker)
        println(data.toString())
        return data
    }
}