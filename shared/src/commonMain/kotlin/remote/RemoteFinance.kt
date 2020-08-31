package remote

import co.touchlab.kampkit.ktor.network
import co.touchlab.stately.ensureNeverFrozen
import domain.data.CompanyData
import domain.data.CompanyNews
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.http.takeFrom
import kotlinx.serialization.json.Json



class RemoteFinance : RemoteFinanceInterface{

    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json{
                ignoreUnknownKeys = true
            })
        }
    }

    init {
        ensureNeverFrozen()
    }


    override suspend fun getCompanyData(ticker: String): CompanyData =
        network {
                client.get<CompanyData> {
                    finnhubData("api/v1/stock/profile2?symbol=$ticker&token=bsp7bq7rh5r8ktikc24g")
                }
        }

    override suspend fun getCompanyNews(ticker: String, from: String, to: String): List<CompanyNews> =
        network {
                val arrayOfCompanyNews = mutableListOf<CompanyNews>()
                client.get<List<CompanyNews>> {
                    finnhubData("api/v1/company-news?symbol=$ticker&from=$from&to=$to&token=bsp7bq7rh5r8ktikc24g")
                }

//                println("ticker is: $ticker")
//                val jsonArray = Json.parseJson(jsonString).jsonArray
//                for(jsonObj in jsonArray){
//                    val news = Json.parse(CompanyNews.serializer(), jsonObj.toString())
//                    println(news.headline)
//                    news.ticker = ticker
//                    arrayOfCompanyNews
//                    arrayOfCompanyNews.add(news)
//                }
//                arrayOfCompanyNews
        }


    private fun HttpRequestBuilder.finnhubData(path: String) {
        url {
            takeFrom("https://finnhub.io/")
            encodedPath = path
        }
    }

}