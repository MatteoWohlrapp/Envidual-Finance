package domain.use_cases

import cache.CompanyDataCache
import cache.CompanyDataCacheInterface
import cache.getThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.RemoteFinance
import remote.RemoteFinanceInterface

class TestUseCase: KoinComponent{


    suspend fun invoke(){
        Job()

        println("Calling CompanyDataCache.selectByTicker in main thread: I am in main thread: ${getThread()}")
        CompanyDataCache(Dispatchers.Default).selectByTickerDebug("AAPL")
//        println("Calling CompanyDataCache.insertDebug in main thread")
//        CompanyDataCache(Dispatchers.Default).insertDebug(listOf())

//        println("Calling RemoteFinance.getCompanyData in main thread")
//        RemoteFinance().getCompanyDataDebug("AAPL")

//        withContext(Dispatchers.Default){
//            println("Calling CompanyDataCache.selectByTicker in background thread: I am in main thread: ${getThread()}")
//            CompanyDataCache(Dispatchers.Default).selectByTickerDebug("AAPL")
////            println("Calling CompanyDataCache.insertDebug in background thread")
////            CompanyDataCache(Dispatchers.Default).insertDebug(listOf())
//
////            println("Calling RemoteFinance.selectByTicker in background thread: I am in main thread: ${getThread()}")
////            RemoteFinance().getCompanyDataDebug("AAPL")
//        }
    }
}