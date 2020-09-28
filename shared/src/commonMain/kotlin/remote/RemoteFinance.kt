package remote

import co.touchlab.stately.freeze
import co.touchlab.stately.isFrozen
import domain.data.CompanyData
import domain.data.CompanyNews
import domain.use_cases.backgroundDispatcher
import domain.use_cases.mainDispatcher
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

class RemoteFinance(private val client: HttpClient) : RemoteFinanceInterface {

    override suspend fun getCompanyData(ticker: String): CompanyData {
        return try {
            withContext(mainDispatcher) {
                client.get {
                    finnhubData("api/v1/stock/profile2?symbol=$ticker&token=bsp7bq7rh5r8ktikc24g")
                }
            }
        } catch (e: NullPointerException) {
            CompanyData()
        } catch (e: ClientRequestException) {
            throw RequestLimitReachedException("Too many requests, please try again in a moment.")
        }
        // we know that Throwable should be the networkException
        catch (t: Throwable) {
            throw NoInternetConnectionException("No internet connection!")
        }
    }

    override suspend fun getCompanyNews(
        ticker: String,
        from: String,
        to: String
    ): List<CompanyNews> {
        return try {
            withContext(mainDispatcher) {
                client.get {
                    finnhubData("api/v1/company-news?symbol=$ticker&from=$from&to=$to&token=bsp7bq7rh5r8ktikc24g")
                }
            }
        } catch (e: NullPointerException) {
            listOf()
        } catch (e: ClientRequestException) {
            throw RequestLimitReachedException("Too many requests, please try again in a moment.")
        } catch (t: Throwable) {
            throw NoInternetConnectionException("No internet connection!")
        }
    }

    override suspend fun isClientFrozen(): Boolean {
        return client.isFrozen
//        return true
    }

    private fun HttpRequestBuilder.finnhubData(path: String) {
        url {
            takeFrom("https://finnhub.io/")
            encodedPath = path
        }
    }

}
