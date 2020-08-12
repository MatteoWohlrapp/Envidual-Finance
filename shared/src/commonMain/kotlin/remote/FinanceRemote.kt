package remote

import co.touchlab.kampkit.ktor.network
import co.touchlab.stately.ensureNeverFrozen
import domain.data.CompanyData
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.http.takeFrom
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration


class FinanceRemote : RemoteFinanceInterface{

    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json(JsonConfiguration(ignoreUnknownKeys = true)))
        }
//        install(Logging) {
//            logger = object : Logger {
//                override fun log(message: String) {
////                    log("").v("Network") { message }
////                    log("")
//                }
//            }
//
//            level = LogLevel.INFO
//        }
    }

    init {
        ensureNeverFrozen()
    }


    override suspend fun getCompanyData(symbol: String): CompanyData =
        network {
            try {
                client.get<CompanyData> {
                    companyData("api/v1/stock/profile2?symbol=$symbol&token=bsp7bq7rh5r8ktikc24g")
                }
            } catch(e: kotlinx.serialization.MissingFieldException){
                return@network CompanyData()
            }
        }

    private fun HttpRequestBuilder.companyData(path: String) {
        url {
            takeFrom("https://finnhub.io/")
            encodedPath = path
        }
    }
}

//internal expect suspend fun <R> network(block: suspend () -> R): R