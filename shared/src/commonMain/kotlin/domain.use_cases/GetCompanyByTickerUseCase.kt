package domain.use_cases

import co.example.envidual.finance.touchlab.db.Explorecompanydata
import domain.data.CompanyData
import remote.RemoteFinanceInterface
import org.koin.core.KoinComponent
import org.koin.core.inject
import sql.ExploreDatabaseHelper


class GetCompanyByTickerUseCase : KoinComponent {

    private val remoteFinance : RemoteFinanceInterface by inject()
    private val dbHelperExplore : ExploreDatabaseHelper by inject()

    suspend fun invoke(ticker: String) : CompanyData {
        var data: CompanyData
        println("About to access data")
        val companiesForTicker: List<Explorecompanydata> = dbHelperExplore.selectByTicker(ticker)
        println("ticker is $ticker and the list contains $companiesForTicker")
        if (companiesForTicker.isEmpty()) {
            // the company was not found in the database, we need to fetch from remote
            println("found no data for the ticker")
            data = remoteFinance.getCompanyData(ticker)
            dbHelperExplore.insertExploreCompanyData(listOf(data))
        } else {
            // the company is inside of the database and we can get it from there
            println("found the company, about to get it from the database")
            val companyInfos: Explorecompanydata = companiesForTicker[0]
            data = CompanyData(
                companyInfos.country,
                null,
                companyInfos.finnhubIndustry,
                companyInfos.ipo,
                null,
                companyInfos.marketCapitalization!!.toFloat(),
                companyInfos.name,
                companyInfos.ticker
            )
        }
        println(data.toString())
        return data
    }
}