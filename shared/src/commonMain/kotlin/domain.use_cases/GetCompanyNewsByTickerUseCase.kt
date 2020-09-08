package domain.use_cases

import cache.CompanyNewsCacheInterface
import co.touchlab.stately.freeze
import domain.data.CompanyNews
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.koin.core.Koin
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.RemoteFinanceInterface

class GetCompanyNewsByTickerUseCase( private val companyNewsCache: CompanyNewsCacheInterface,
                                     private val remoteFinance: RemoteFinanceInterface) {


    suspend fun invoke(ticker: String): Flow<List<CompanyNews>> {
            return withContext(backgroundDispatcher) {
                return@withContext flow {
                val data = companyNewsCache.selectByTicker(ticker)
                if (data.size <= 10){
                    println("selected news from cache")
                    emit(data)
                }
                else {
                    val lastEntry = data[9]
                    companyNewsCache.deleteByTickerAndDateTime(ticker, lastEntry.datetime!!)
                    emit(data.subList(0, 10))
                }
                try {
                    remoteFinance.freeze()
                } catch (e: Throwable) {
                    println(e.message)
                }
                    var companyNews = listOf<CompanyNews>()
                companyNews =
                    remoteFinance.getCompanyNews(ticker, getYesterdaysDate(), getTodaysDate())

                    println("In companyNews, fetching data")
                val companyNewsWithCorrectTicker = mutableListOf<CompanyNews>()
                for (news in companyNews)
                    companyNewsWithCorrectTicker.add(news.copy(ticker = ticker))

                companyNewsCache.insert(companyNewsWithCorrectTicker)

                companyNewsCache.selectByTickerAsFlow(ticker).collect {
                    if (it.size <= 10){
                        println("collecting flow from news cache")
                        emit(it)
                    }
                    else {
                        val lastEntry = it[9]
                        companyNewsCache.deleteByTickerAndDateTime(ticker, lastEntry.datetime!!)
                        emit(it.subList(0, 10))
                    }

                }
            }
        }
    }
}

expect fun getTodaysDate(): String

expect fun getYesterdaysDate(): String
