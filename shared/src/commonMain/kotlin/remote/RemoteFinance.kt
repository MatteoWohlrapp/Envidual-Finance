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
import kotlinx.serialization.json.JsonConfiguration


class RemoteFinance : RemoteFinanceInterface{

    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json(JsonConfiguration(ignoreUnknownKeys = true)))
        }
    }

    init {
        ensureNeverFrozen()
    }


    override suspend fun getCompanyData(ticker: String): CompanyData =
        network {
            try {
                client.get<CompanyData> {
                    finnhubData("api/v1/stock/profile2?symbol=$ticker&token=bsp7bq7rh5r8ktikc24g")
                }
            } catch(e: kotlinx.serialization.MissingFieldException){
                throw CompanyNotFoundException("No company found.")
            }
        }

    override suspend fun getCompanyNews(ticker: String): List<CompanyNews> =
        network {
            try {
                val arrayOfCompanyNews = mutableListOf<CompanyNews>()
                val jsonString = client.get<String> {
                    finnhubData("api/v1/company-news?symbol=$ticker&from=2020-08-28&to=2020-08-28&token=bsp7bq7rh5r8ktikc24g")
                }
                println("ticker is: $ticker")
                val jsonArray = Json.parseJson(jsonString).jsonArray
                for(jsonObj in jsonArray){
                    val news = Json.parse(CompanyNews.serializer(), jsonObj.toString())
                    println(news.headline)
                    news.ticker = ticker
                    arrayOfCompanyNews
                    arrayOfCompanyNews.add(news)
                }
                arrayOfCompanyNews
            } catch(e: kotlinx.serialization.MissingFieldException){
                throw CompanyNotFoundException("No company found.")
            }
        }


    private fun HttpRequestBuilder.finnhubData(path: String) {
        url {
            takeFrom("https://finnhub.io/")
            encodedPath = path
        }
    }

}