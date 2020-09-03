package domain.use_cases

import cache.CompanyDataCache
import cache.CompanyDataCacheInterface
import co.touchlab.stately.freeze
import com.squareup.sqldelight.internal.AtomicBoolean
import domain.data.CompanyData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.withContext
import remote.RemoteFinanceInterface
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.CompanyNotFoundException
import remote.RemoteFinance


class GetCompanyByTickerUseCase : KoinComponent {

    private val remoteFinance: RemoteFinanceInterface by inject()
    private val companyDataCache: CompanyDataCacheInterface by inject()

    suspend fun invoke(ticker: String) {
            val upperCaseTicker = ticker.toUpperCase()

            println("About to access data")
            var companyByGivenTicker = listOf<CompanyData>()

            companyByGivenTicker = companyDataCache.selectByTicker(upperCaseTicker)
            println("ticker is $upperCaseTicker and the list from the database contains $companyByGivenTicker")

            if (companyByGivenTicker.isEmpty()) {
                // the company was not found in the database, we need to fetch from remote
                println("found no data for the given ticker in the table")

//                val companyDataFromRemote = remoteFinance.getCompanyData(upperCaseTicker)
                val companyDataFromRemote = CompanyData()

                println("Quick restart works perfectly!")

                val test = remoteFinance.getCompanyData("AAPL")
                println(test.name)


                var companiesByRemoteTicker: List<CompanyData> = listOf()

                println("Requested from remote: ${companyDataFromRemote.name}")

//            checking if company was found
                if (companyDataFromRemote.name != null)
                    companiesByRemoteTicker =
                        companyDataCache.selectByTicker(companyDataFromRemote.ticker!!)
                else
                    throw CompanyNotFoundException("No company found.")

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