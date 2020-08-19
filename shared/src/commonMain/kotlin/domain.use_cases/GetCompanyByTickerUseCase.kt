package domain.use_cases

import domain.data.CompanyData
import remote.RemoteFinanceInterface
import org.koin.core.KoinComponent
import org.koin.core.inject
import sql.DatabaseHelper


class GetCompanyByTickerUseCase : KoinComponent {

    private val remoteFinance : RemoteFinanceInterface by inject()
    private val dbHelper : DatabaseHelper by inject()

    suspend fun invoke(ticker: String) : CompanyData {
        val upperCaseTicker = ticker.toUpperCase()
        var data: CompanyData
        println("About to access data")
        val companiesByTicker = dbHelper.selectByTicker(upperCaseTicker)
        println("ticker is $upperCaseTicker and the list from the database contains $companiesByTicker")

        if (companiesByTicker.isEmpty()) {
            // the company was not found in the database, we need to fetch from remote
            println("found no data for the ticker in the table")
            data = remoteFinance.getCompanyData(upperCaseTicker)
            dbHelper.insertCompany(listOf(data))
        }
        else {
            println("found data for ticker in the table")
            val companyInfos = companiesByTicker.first()

            data = CompanyData(
                companyInfos.country,
                companyInfos.currency,
                companyInfos.finnhubIndustry,
                companyInfos.ipo,
                companyInfos.logo,
                companyInfos.marketCapitalization!!.toFloat(),
                companyInfos.name,
                companyInfos.ticker
            )
        }
        println(data.toString())
        return data
    }
}