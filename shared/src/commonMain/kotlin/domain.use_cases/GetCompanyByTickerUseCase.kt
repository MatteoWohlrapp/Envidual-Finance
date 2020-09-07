package domain.use_cases

import cache.CompanyDataCache
import cache.CompanyDataCacheInterface
import cache.getThread
import co.touchlab.stately.ensureNeverFrozen
import co.touchlab.stately.freeze
import co.touchlab.stately.isFrozen
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
        companyDataCache.freeze()
//        remoteFinance.freeze()
        withContext(Dispatchers.Default) {
            val upperCaseTicker = ticker.toUpperCase()
            var companyByGivenTicker = listOf<CompanyData>()
            println("About to go into companyDataCache")
            companyByGivenTicker = companyDataCache.selectByTicker(upperCaseTicker)

            if (companyByGivenTicker.isEmpty()) {
                // the company was not found in the database, we need to fetch from remote
                println("found no data for the given ticker in the table")
                withContext(Dispatchers.Main) {
                    println("I'm back on main")
                    val companyDataFromRemote = remoteFinance.getCompanyData(upperCaseTicker)
//
//                    var companiesByRemoteTicker: List<CompanyData> = listOf()
//
//                    println("Requested from remote: ${companyDataFromRemote.name}")
//
                }

////            checking if company was found
//                if (companyDataFromRemote.name != null)
//                    companiesByRemoteTicker =
//                        companyDataCache.selectByTicker(companyDataFromRemote.ticker!!)
//                else
//                    throw CompanyNotFoundException("No company found.")
//
//                if (companiesByRemoteTicker.isEmpty()) {
//                    println("found no data for the remote ticker in the table")
//                    companyDataCache.insert(
//                        listOf(
//                            companyDataFromRemote.copy(
//                                isSearched = true,
//                                lastSearched = getTimestamp()
//                            )
//                        )
//                    )
//                } else {
//                    println("found data for the remote ticker in the table")
//                    val companyDataFromDatabaseByRemoteTicker = companiesByRemoteTicker.first()
//
//                    val time = getTimestamp()
//
//                    companyDataCache.updateIsSearchedByTicker(
//                        true,
//                        companyDataFromDatabaseByRemoteTicker.ticker!!
//                    )
//                    companyDataCache.updateLastSearchedByTicker(
//                        time,
//                        companyDataFromDatabaseByRemoteTicker.ticker!!
//                    )
//                }
//            } else {
//                println("found data for ticker in the table")
//                val companyDataFromDatabaseByGivenTicker = companyByGivenTicker.first()
//
//                val time = getTimestamp()
//
//                companyDataCache.updateIsSearchedByTicker(
//                    true,
//                    companyDataFromDatabaseByGivenTicker.ticker!!
//                )
//                companyDataCache.updateLastSearchedByTicker(
//                    time,
//                    companyDataFromDatabaseByGivenTicker.ticker!!
//                )
            }
        }
    }
}