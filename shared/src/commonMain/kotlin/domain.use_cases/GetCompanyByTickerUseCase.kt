package domain.use_cases

import cache.CompanyDataCacheInterface
import co.touchlab.stately.freeze
import co.touchlab.stately.isFrozen
import domain.data.CompanyData
import io.ktor.client.*
import io.ktor.client.features.DefaultRequest.Feature.install
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import kotlinx.atomicfu.atomic
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

    suspend fun invoke(ticker: String) {
//        val tempCache = companyDataCache
//        val tempRemote = remoteFinance
//        println("Use case: $isFrozen")
//        println("Cache: ${companyDataCache.isFrozen}")
//        println("Remote: ${remoteFinance.isFrozen}")
//        println("-----Outside of withContext-----")
//        withContext(Dispatchers.Default) {
//            println("-----Inside of withContext-----")
//            companyDataCache.deleteByTicker("AAPL")
////            withContext(Dispatchers.Main) {
////                val company = remoteFinance.getCompanyData("AAPL")
////                println(company.name)
////            }
//        }

        val tempCompanyDataCache = companyDataCache
        val tempRemoteFinance = remoteFinance
//        necessary to run for the first time

        try {
            remoteFinance.freeze()
        } catch(e: Throwable){
            println(e.message)
        }

        println("Use case: $isFrozen")
        println("Cache: ${companyDataCache.isFrozen}")
        println("Remote: ${remoteFinance.isFrozen}")
        println("http: ${remoteFinance.isClientFrozen()}")

        withContext(Dispatchers.Default) {

            val upperCaseTicker = ticker.toUpperCase()
            var companyByGivenTicker = listOf<CompanyData>()
            println("About to go into companyDataCache")
            companyByGivenTicker = tempCompanyDataCache.selectByTicker(upperCaseTicker)
            println("companyByGivenTicker.isEmpty(): ${companyByGivenTicker.isEmpty()}")

            var companyDataFromRemote = CompanyData()
            var companiesByRemoteTicker = listOf<CompanyData>()

            if (companyByGivenTicker.isEmpty()) {
                // the company was not found in the database, we need to fetch from remote
                println("found no data for the given ticker in the table")

                companyDataFromRemote = fetchCompanyDataFromRemote(upperCaseTicker)

                println("Requested from remote: ${companyDataFromRemote.name}")


//            checking if company was found
                if (companyDataFromRemote.name != null) {
                    companiesByRemoteTicker =
                        tempCompanyDataCache.selectByTicker(companyDataFromRemote.ticker!!)
                } else
                    throw CompanyNotFoundException("No company found.")

                if (companiesByRemoteTicker.isEmpty()) {
                    println("found no data for the remote ticker in the table")
                    tempCompanyDataCache.insert(
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

                    tempCompanyDataCache.updateIsSearchedByTicker(
                        true,
                        companyDataFromDatabaseByRemoteTicker.ticker!!
                    )
                    tempCompanyDataCache.updateLastSearchedByTicker(
                        time,
                        companyDataFromDatabaseByRemoteTicker.ticker!!
                    )
                }
            } else {
                println("found data for ticker in the table")
                val companyDataFromDatabaseByGivenTicker = companyByGivenTicker.first()

                val time = getTimestamp()

                tempCompanyDataCache.updateIsSearchedByTicker(
                    true,
                    companyDataFromDatabaseByGivenTicker.ticker!!
                )
                tempCompanyDataCache.updateLastSearchedByTicker(
                    time,
                    companyDataFromDatabaseByGivenTicker.ticker!!
                )
            }
        }
    }

    private suspend fun fetchCompanyDataFromRemote(upperCaseTicker: String) =
        withContext(Dispatchers.Main) {
            remoteFinance.getCompanyData(upperCaseTicker)
        }

}