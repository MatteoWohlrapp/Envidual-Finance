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
import remote.RemoteFinanceInterface

class GetCompanyNewsByTickerUseCase(
    private val companyNewsCache: CompanyNewsCacheInterface,
    private val remoteFinance: RemoteFinanceInterface
) {


    suspend fun invoke(ticker: String): Flow<List<CompanyNews>> {
        try {
            remoteFinance.freeze()
        } catch (e: Throwable) {
            println(e.message)
        }
        return withContext(backgroundDispatcher) {

            try {
                remoteFinance.freeze()
            } catch (e: Throwable) {
                println(e.message)
            }

            return@withContext flow {

                var data: List<CompanyNews> = companyNewsCache.selectByTicker(ticker)

                if (data.isNotEmpty())
                    emit(data)

                if (data.isNotEmpty()) {
                    if (data[0].lastRequested != null) {
                        if (data[0].lastRequested!! > getTimestamp() - 300) {
                            getNewsOfTheLastSevenDays(ticker)
                            emit(companyNewsCache.selectByTicker(ticker))
                        }
                    }
                } else {
                    getNewsOfTheLastSevenDays(ticker)
                    emit(companyNewsCache.selectByTicker(ticker))
                }
            }
        }
    }

    private suspend fun getNewsOfTheLastSevenDays(ticker: String) {

        val to = getDayNumberOfDaysBefore(0)
        val from = getDayNumberOfDaysBefore(6)

        try {
            remoteFinance.freeze()
        } catch (e: Throwable) {
            println(e.message)
        }
        val companyNewsFromRemote = remoteFinance.getCompanyNews(ticker, from, to)

        companyNewsCache.deleteByTicker(ticker)

        val time = getTimestamp()
        companyNewsCache.insert(companyNewsFromRemote.map {
            it.copy(ticker = ticker, lastRequested = time)
        })
    }
}

expect fun getDayNumberOfDaysBefore(numberOfDays: Int): String
