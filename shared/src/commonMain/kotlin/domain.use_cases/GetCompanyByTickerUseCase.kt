package domain.use_cases

import cache.CompanyDataCacheInterface
import cache.getThread
import co.touchlab.stately.freeze
import co.touchlab.stately.isFrozen
import domain.data.CompanyData
import io.ktor.client.*
import io.ktor.client.features.DefaultRequest.Feature.install
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import remote.CompanyNotFoundException
import remote.RemoteFinance
import remote.RemoteFinanceInterface
import kotlin.native.concurrent.ThreadLocal


class GetCompanyByTickerUseCase(
    private val remoteFinance: RemoteFinanceInterface,
    private val companyDataCache: CompanyDataCacheInterface


) {

    @Throws(Exception::class, CompanyNotFoundException::class)
    suspend fun invoke(ticker: String) {

        println("Use case: $isFrozen")
        println("Cache: ${companyDataCache.isFrozen}")
        println("Remote: ${remoteFinance.isFrozen}")
        println("http: ${remoteFinance.isClientFrozen()}")
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

            var companyDataFromRemote = CompanyData()
            var companiesByRemoteTicker = listOf<CompanyData>()

            if (companyByGivenTicker.isEmpty()) {
                // the company was not found in the database, we need to fetch from remote
                println("found no data for the given ticker in the table")

                companyDataFromRemote = remoteFinance.getCompanyData(upperCaseTicker)

                println("Requested from remote: ${companyDataFromRemote.name}")


//            checking if company was found
                if (companyDataFromRemote.name != null) {
                    companiesByRemoteTicker =
                        companyDataCache.selectByTicker(companyDataFromRemote.ticker!!)
                } else
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
}