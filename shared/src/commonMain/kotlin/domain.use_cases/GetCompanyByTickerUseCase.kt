package domain.use_cases

import cache.CompanyDataCacheInterface
import domain.data.CompanyData
import remote.RemoteFinanceInterface
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.CompanyNotFoundException


class GetCompanyByTickerUseCase : KoinComponent {

    private val remoteFinance: RemoteFinanceInterface by inject()
    private val companyDataCache : CompanyDataCacheInterface by inject()

    suspend fun invoke(ticker: String){
        val upperCaseTicker = ticker.toUpperCase()

        println("About to access data")
        val companyByGivenTicker = companyDataCache.selectByTicker(upperCaseTicker)
        println("ticker is $upperCaseTicker and the list from the database contains $companyByGivenTicker")

        if (companyByGivenTicker.isEmpty()) {
            // the company was not found in the database, we need to fetch from remote
            println("found no data for the given ticker in the table")
            val companyDataFromRemote = remoteFinance.getCompanyData(upperCaseTicker)

            var companiesByRemoteTicker: List<CompanyData>

//            checking if company was found
            if(companyDataFromRemote.name != null)
                companiesByRemoteTicker = companyDataCache.selectByTicker(companyDataFromRemote.ticker!!)
            else
                throw CompanyNotFoundException("No company found.")

            if (companiesByRemoteTicker.isEmpty()) {
                println("found no data for the remote ticker in the table")
                val time = getTimestamp()
                companyDataFromRemote.lastSearched = time
                companyDataFromRemote.isSearched = true

                companyDataCache.insert(listOf(companyDataFromRemote))
            } else {
                println("found data for the remote ticker in the table")
                val companyDataFromDatabaseByRemoteTicker = companiesByRemoteTicker.first()

                val time = getTimestamp()
                companyDataFromDatabaseByRemoteTicker.isSearched = true
                companyDataFromDatabaseByRemoteTicker.lastSearched = time

                companyDataCache.updateIsSearchedByTicker(companyDataFromDatabaseByRemoteTicker.isSearched!!, companyDataFromDatabaseByRemoteTicker.ticker!!)
                companyDataCache.updateLastSearchedByTicker(companyDataFromDatabaseByRemoteTicker.lastSearched!!, companyDataFromDatabaseByRemoteTicker.ticker!!)
            }
        } else {
            println("found data for ticker in the table")
            val companyDataFromDatabaseByGivenTicker = companyByGivenTicker.first()


            val time = getTimestamp()
            companyDataFromDatabaseByGivenTicker.isSearched = true
            companyDataFromDatabaseByGivenTicker.lastSearched = time

            companyDataCache.updateIsSearchedByTicker(companyDataFromDatabaseByGivenTicker.isSearched!!, companyDataFromDatabaseByGivenTicker.ticker!!)
            companyDataCache.updateLastSearchedByTicker(companyDataFromDatabaseByGivenTicker.lastSearched!!, companyDataFromDatabaseByGivenTicker.ticker!!)
        }
    }
}