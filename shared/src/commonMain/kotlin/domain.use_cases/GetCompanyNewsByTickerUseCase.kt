package domain.use_cases

import domain.data.CompanyNews
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.RemoteFinanceInterface
import sql.DatabaseHelper

class GetCompanyNewsByTickerUseCase: KoinComponent {

    val dbHelper:DatabaseHelper by inject()
    val remoteFinance:RemoteFinanceInterface by inject()

    suspend fun invoke(ticker: String): Flow<List<CompanyNews>> = flow{
        val data = dbHelper.selectByTickerFromCompaniesNews(ticker)
        if(data.size <= 10)
            emit(data)
        else
            emit(data.subList(0, 10))

//        val date = getTodaysDate()
//        val companyNews = remoteFinance.getCompanyNews(ticker, date, date)
        val companyNews = remoteFinance.getCompanyNews(ticker, "2020-08-29", "2020-08-29")
        dbHelper.insertCompaniesNews(companyNews)

        dbHelper.selectByTickerFromCompaniesNewsAsFlow(ticker).collect {
            if(it.size <= 10)
                emit(it)
            else
                emit(it.subList(0, 10)) }
    }

}
expect fun getTodaysDate() : String
