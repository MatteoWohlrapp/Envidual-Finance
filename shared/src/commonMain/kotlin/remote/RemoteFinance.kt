package remote

import co.touchlab.stately.isFrozen
import domain.data.CompanyData
import domain.data.CompanyNews
import domain.use_cases.mainDispatcher
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.withContext

class RemoteFinance(private val client: HttpClient) : RemoteFinanceInterface {

    override suspend fun getCompanyData(ticker: String): CompanyData {
        println("Got to getCompanyData in RemoteFinance")
        return withContext(mainDispatcher) {
//            val client = HttpClient {
//                install(JsonFeature) {
//                    serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
//                        ignoreUnknownKeys = true
//                    })
//                }
//            }
            try {
                client.get {
                    finnhubData("api/v1/stock/profile2?symbol=$ticker&token=bsp7bq7rh5r8ktikc24g")
                }
            } catch (e: NullPointerException) {
                return@withContext CompanyData()
            }
        }
    }

    override suspend fun getCompanyNews(
        ticker: String,
        from: String,
        to: String
    ): List<CompanyNews> =
        withContext(mainDispatcher) {
//            val client = HttpClient {
//                install(JsonFeature) {
//                    serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
//                        ignoreUnknownKeys = true
//                    })
//                }
//            }
            try {
                client.get {
                    finnhubData("api/v1/company-news?symbol=$ticker&from=$from&to=$to&token=bsp7bq7rh5r8ktikc24g")
                }
            } catch (e: NullPointerException) {
                return@withContext listOf()
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