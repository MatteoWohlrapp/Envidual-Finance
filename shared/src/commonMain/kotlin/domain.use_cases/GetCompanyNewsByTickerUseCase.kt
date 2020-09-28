package domain.use_cases

import cache.CompanyNewsCacheInterface
import co.touchlab.stately.freeze
import domain.data.CompanyData
import domain.data.CompanyNews
import io.ktor.client.features.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.koin.core.Koin
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.CompanyNewsNotFoundException
import remote.NoInternetConnectionException
import remote.RemoteFinanceInterface

class GetCompanyNewsByTickerUseCase(
    private val companyNewsCache: CompanyNewsCacheInterface,
    private val remoteFinance: RemoteFinanceInterface
) {

    @Throws(Exception::class, CompanyNewsNotFoundException::class, NoInternetConnectionException::class)
    suspend fun invoke(ticker: String): List<CompanyNews> {
        return withContext(backgroundDispatcher) {
            var data: List<CompanyNews> = companyNewsCache.selectByTicker(ticker)

            if (data.isEmpty()) {
                getNewsOfTheLastSevenDays(ticker)
            } else {
                if(data[0].lastRequested != null){
                    if(data[0].lastRequested!! > getTimestamp() - 300){
                        getNewsOfTheLastSevenDays(ticker)
                    }
                } else
                    getNewsOfTheLastSevenDays(ticker)
            }

            return@withContext companyNewsCache.selectByTicker(ticker)
        }
    }

    private suspend fun getNewsOfTheLastSevenDays(ticker: String) {

        val to = getDayNumberOfDaysBefore(0)
        val from = getDayNumberOfDaysBefore(6)

//        try {
//            remoteFinance.freeze()
//        } catch (e: Throwable) {
//            println(e.message)
//        }
        val companyNewsFromRemote = remoteFinance.getCompanyNews(ticker, from, to)

        companyNewsCache.deleteByTicker(ticker)

        if (companyNewsFromRemote.isEmpty())
            throw CompanyNewsNotFoundException("No company news found for the last seven days.")

        val time = getTimestamp()
        companyNewsCache.insert(companyNewsFromRemote.map {
            it.copy(ticker = ticker, lastRequested = time)
        })
    }
}

expect fun getDayNumberOfDaysBefore(numberOfDays: Int): String
