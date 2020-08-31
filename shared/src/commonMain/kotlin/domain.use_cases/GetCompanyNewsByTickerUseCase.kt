package domain.use_cases

import cache.CompanyNewsCacheInterface
import domain.data.CompanyNews
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.RemoteFinanceInterface

class GetCompanyNewsByTickerUseCase : KoinComponent {

    private val companyNewsCache: CompanyNewsCacheInterface by inject()
    val remoteFinance: RemoteFinanceInterface by inject()

    suspend fun invoke(ticker: String): Flow<List<CompanyNews>> = flow {
        val data = companyNewsCache.selectByTicker(ticker)
        if (data.size <= 10)
            emit(data)
        else {
            val lastEntry = data[9]
            companyNewsCache.deleteByTickerAndDateTime(ticker, lastEntry.datetime!!)
            emit(data.subList(0, 10))
        }

//        val date = getTodaysDate()
//        val companyNews = remoteFinance.getCompanyNews(ticker, date, date)
        val companyNews = remoteFinance.getCompanyNews(ticker, "2020-08-30", "2020-08-31")
        val companyNewsWithCorrectTicker = mutableListOf<CompanyNews>()
        for (news in companyNews)
            companyNewsWithCorrectTicker.add(news.copy(ticker = ticker))

        println(companyNews.toString())

        companyNewsCache.insert(companyNewsWithCorrectTicker)

        companyNewsCache.selectByTickerAsFlow(ticker).collect {
            if (it.size <= 10)
                emit(it)
            else {
                val lastEntry = it[9]
                companyNewsCache.deleteByTickerAndDateTime(ticker, lastEntry.datetime!!)
                emit(it.subList(0, 10))
            }
        }
    }

}

expect fun getTodaysDate(): String
