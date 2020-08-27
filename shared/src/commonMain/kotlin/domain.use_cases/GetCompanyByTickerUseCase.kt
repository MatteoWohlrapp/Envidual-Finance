package domain.use_cases

import co.example.envidual.finance.touchlab.db.Companies
import domain.data.CompanyData
import remote.RemoteFinanceInterface
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.NoCompanyFoundException
import sql.DatabaseHelper


class GetCompanyByTickerUseCase : KoinComponent {

    private val remoteFinance: RemoteFinanceInterface by inject()
    private val dbHelper: DatabaseHelper by inject()

    suspend fun invoke(ticker: String): CompanyData {
        val upperCaseTicker = ticker.toUpperCase()
        var data: CompanyData
        println("About to access data")
        val companiesByGivenTicker = dbHelper.selectByTicker(upperCaseTicker)
        println("ticker is $upperCaseTicker and the list from the database contains $companiesByGivenTicker")

        if (companiesByGivenTicker.isEmpty()) {
            // the company was not found in the database, we need to fetch from remote
            println("found no data for the ticker in the table")
            data = remoteFinance.getCompanyData(upperCaseTicker)
            var companiesByRemoteTicker = listOf<Companies>()
            if(data.name != null)
                companiesByRemoteTicker = dbHelper.selectByTicker(data.ticker!!)
            else
                throw NoCompanyFoundException("No company found.")

            if (companiesByRemoteTicker.isEmpty()) {
                val time = getTimestamp()
                println("Timestamp is $time")
                dbHelper.insertCompany(listOf(data), time)
            } else {
                val company = companiesByRemoteTicker.first()

                data = CompanyData(
                    company.country,
                    company.currency,
                    company.finnhubIndustry,
                    company.ipo,
                    company.logo,
                    company.marketCapitalization!!.toFloat(),
                    company.name,
                    company.ticker
                )

                val time = getTimestamp()
                data.isSearched = true
                dbHelper.changeIsSearchedForTicker(data.isSearched!!, data.ticker!!)
                println("Timestamp is $time")
                dbHelper.changeLastSearched(time, data.ticker!!)
            }
        } else {
            println("found data for ticker in the table")
            val company = companiesByGivenTicker.first()

            data = CompanyData(
                company.country,
                company.currency,
                company.finnhubIndustry,
                company.ipo,
                company.logo,
                company.marketCapitalization!!.toFloat(),
                company.name,
                company.ticker
            )

            val time = getTimestamp()
            data.isSearched = true
            dbHelper.changeIsSearchedForTicker(data.isSearched!!, data.ticker!!)
            println("Timestamp is $time")
            dbHelper.changeLastSearched(time, data.ticker!!)
        }
        println(data.toString())
        return data
    }
}