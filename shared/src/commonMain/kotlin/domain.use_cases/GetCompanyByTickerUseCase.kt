package domain.use_cases

import cache.CompanyDataCacheInterface
import co.touchlab.stately.freeze
import domain.data.CompanyData
import kotlinx.coroutines.withContext
import remote.CompanyDataNotFoundException
import remote.RemoteFinanceInterface


class GetCompanyByTickerUseCase(
    private val remoteFinance: RemoteFinanceInterface,
    private val companyDataCache: CompanyDataCacheInterface


) {

    @Throws(Exception::class, CompanyDataNotFoundException::class)
    suspend fun invoke(ticker: String) {
        //        necessary to run for the first time, remote Finance gets frozen, http client throws exception but its caught. No further freezing, because remote finance is frozen
        try {
            remoteFinance.freeze()
        } catch (e: Throwable) {
            println(e.message)
        }

        withContext(backgroundDispatcher) {

            val upperCaseTicker = ticker.toUpperCase()
            var companyByGivenTicker = listOf<CompanyData>()
            println("About to go into companyDataCache")
            companyByGivenTicker = companyDataCache.selectByTicker(upperCaseTicker)
            println("companyByGivenTicker.isEmpty(): ${companyByGivenTicker.isEmpty()}")

            if (companyByGivenTicker.isEmpty()) {
                // the company was not found in the database, we need to fetch from remote
                println("found no data for the given ticker in the table")

                try {
                    remoteFinance.freeze()
                } catch (e: Throwable) {
                    println(e.message)
                }

                val companyDataFromRemote = remoteFinance.getCompanyData(upperCaseTicker)

                println("Requested from remote: ${companyDataFromRemote.name}")

                var companiesByRemoteTicker = listOf<CompanyData>()

//            checking if company was found
                if (companyDataFromRemote.name != null) {
                    companiesByRemoteTicker =
                        companyDataCache.selectByTicker(companyDataFromRemote.ticker!!)
                } else
                    throw CompanyDataNotFoundException("No company found.")

                if (companiesByRemoteTicker.isEmpty()) {
                    println("found no data for the remote ticker in the table")
                    companyDataCache.insert(
                        listOf(
                            companyDataFromRemote.copy(
                                isSearched = true,
                                lastSearched = getTimestamp()
                            )
                        )
                    )
                } else {
                    println("found data for the remote ticker in the table")
                    val companyDataFromDatabaseByRemoteTicker = companiesByRemoteTicker.first()

                    val time = getTimestamp()

                    companyDataCache.updateIsSearchedByTicker(
                        true,
                        companyDataFromDatabaseByRemoteTicker.ticker!!
                    )
                    companyDataCache.updateLastSearchedByTicker(
                        time,
                        companyDataFromDatabaseByRemoteTicker.ticker!!
                    )
                }
            } else {
                println("found data for ticker in the table")
                val companyDataFromDatabaseByGivenTicker = companyByGivenTicker.first()

                val time = getTimestamp()

                companyDataCache.updateIsSearchedByTicker(
                    true,
                    companyDataFromDatabaseByGivenTicker.ticker!!
                )
                companyDataCache.updateLastSearchedByTicker(
                    time,
                    companyDataFromDatabaseByGivenTicker.ticker!!
                )
            }
        }
    }
}