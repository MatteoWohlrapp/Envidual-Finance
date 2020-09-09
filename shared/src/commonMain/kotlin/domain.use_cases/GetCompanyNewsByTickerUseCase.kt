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

            return@withContext flow {
                var data = listOf<CompanyNews>()
                data = companyNewsCache.selectByTicker(ticker)
                emit(data)

                try {
                    remoteFinance.freeze()
                } catch (e: Throwable) {
                    println(e.message)
                }
                var numberOfDays = -1

                var collectedCompanyNews = listOf<CompanyNews>()

                companyNewsCache.deleteByTicker(ticker)

                do {
                    ++numberOfDays
                    var to = getDayNumberOfDaysBefore(0)
                    var from = getDayNumberOfDaysBefore(numberOfDays)

                    println("Number of days is: $numberOfDays")

                    try {
                        collectedCompanyNews = remoteFinance.getCompanyNews(ticker, from, to)
                    } catch(e: ClientRequestException){
                        break
                    }
                    println("collectedCompanyNews has ${collectedCompanyNews.size} elements")

                    companyNewsCache.insert(
                        collectedCompanyNews.map {
                            it.copy(ticker = ticker)
                        })
                } while (companyNewsCache.selectByTicker(ticker).size <= 9 && numberOfDays < 32)


                collectedCompanyNews = companyNewsCache.selectByTicker(ticker)
                println("list from database has ${collectedCompanyNews.size} elements")

                if (collectedCompanyNews.size <= 10) {
                    emit(collectedCompanyNews)
                } else {
                    val lastEntry = collectedCompanyNews[9]
                    companyNewsCache.deleteByTickerAndDateTime(ticker, lastEntry.datetime!!)
                    emit(collectedCompanyNews.subList(0, 10))
                }
            }
        }
    }

    private fun removeDuplicates(list: List<CompanyNews>): List<CompanyNews> {
        val returnList = mutableListOf<CompanyNews>()
        val stringList = mutableListOf<String>()

        for (news in list) {
            if (!stringList.contains(news.headline)) {
                stringList.add(news.headline!!)
                returnList.add(news)
            }
        }
        return returnList
    }
}

expect fun getDayNumberOfDaysBefore(numberOfDays: Int): String
