package domain.use_cases

import domain.data.CompanyData
import remote.RemoteFinanceInterface
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.NoCompanyFoundException
import sql.DatabaseHelper


class GetCompanyByTickerUseCase : KoinComponent {

    private val remoteFinance: RemoteFinanceInterface by inject()
    private val dbHelper: DatabaseHelper by inject()

    suspend fun invoke(ticker: String){
        val upperCaseTicker = ticker.toUpperCase()

        println("About to access data")
        val companyByGivenTicker = dbHelper.selectByTicker(upperCaseTicker)
        println("ticker is $upperCaseTicker and the list from the database contains $companyByGivenTicker")

        if (companyByGivenTicker.isEmpty()) {
            // the company was not found in the database, we need to fetch from remote
            println("found no data for the given ticker in the table")
            val companyDataFromRemote = remoteFinance.getCompanyData(upperCaseTicker)

            var companiesByRemoteTicker: List<CompanyData>

//            checking if company was found
            if(companyDataFromRemote.name != null)
                companiesByRemoteTicker = dbHelper.selectByTicker(companyDataFromRemote.ticker!!)
            else
                throw NoCompanyFoundException("No company found.")

            if (companiesByRemoteTicker.isEmpty()) {
                println("found no data for the remote ticker in the table")
                val time = getTimestamp()
                companyDataFromRemote.lastSearched = time
                companyDataFromRemote.isSearched = true

                dbHelper.insertCompanies(listOf(companyDataFromRemote))
            } else {
                println("found data for the remote ticker in the table")
                val companyDataFromDatabaseByRemoteTicker = companiesByRemoteTicker.first()

                val time = getTimestamp()
                companyDataFromDatabaseByRemoteTicker.isSearched = true
                companyDataFromDatabaseByRemoteTicker.lastSearched = time

                dbHelper.changeIsSearchedForTicker(companyDataFromDatabaseByRemoteTicker.isSearched!!, companyDataFromDatabaseByRemoteTicker.ticker!!)
                dbHelper.changeLastSearched(companyDataFromDatabaseByRemoteTicker.lastSearched!!, companyDataFromDatabaseByRemoteTicker.ticker!!)
            }
        } else {
            println("found data for ticker in the table")
            val companyDataFromDatabaseByGivenTicker = companyByGivenTicker.first()


            val time = getTimestamp()
            companyDataFromDatabaseByGivenTicker.isSearched = true
            companyDataFromDatabaseByGivenTicker.lastSearched = time

            dbHelper.changeIsSearchedForTicker(companyDataFromDatabaseByGivenTicker.isSearched!!, companyDataFromDatabaseByGivenTicker.ticker!!)
            dbHelper.changeLastSearched(companyDataFromDatabaseByGivenTicker.lastSearched!!, companyDataFromDatabaseByGivenTicker.ticker!!)
        }
    }
}