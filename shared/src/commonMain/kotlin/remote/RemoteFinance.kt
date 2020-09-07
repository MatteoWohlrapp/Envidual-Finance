package remote

import cache.getThread
import co.touchlab.kampkit.ktor.network
import co.touchlab.stately.ensureNeverFrozen
import com.squareup.sqldelight.internal.Atomic
import com.squareup.sqldelight.internal.getValue
import domain.data.CompanyData
import domain.data.CompanyNews
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.http.takeFrom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlin.coroutines.coroutineContext
import kotlin.native.concurrent.ThreadLocal


class RemoteFinance : RemoteFinanceInterface {

//    init {
//        ensureNeverFrozen()
//    }

//    private val client = Atomic<HttpClient>(
//        HttpClient {
//            install(JsonFeature) {
//                serializer = KotlinxSerializer(Json {
//                    ignoreUnknownKeys = true
//                })
//            }
//        }
//    )

    private val client =
        HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

    override suspend fun getCompanyData(ticker: String): CompanyData {
        println("Got to getCompanyData")
        return network {
            client.get<CompanyData> {
                finnhubData("api/v1/stock/profile2?symbol=$ticker&token=bsp7bq7rh5r8ktikc24g")
            }
        }
    }

    override suspend fun getCompanyNews(
        ticker: String,
        from: String,
        to: String
    ): List<CompanyNews> =
        network {
            client.get<List<CompanyNews>> {
                finnhubData("api/v1/company-news?symbol=$ticker&from=$from&to=$to&token=bsp7bq7rh5r8ktikc24g")
            }
        }


    private fun HttpRequestBuilder.finnhubData(path: String) {
        url {
            takeFrom("https://finnhub.io/")
            encodedPath = path
        }
    }

}