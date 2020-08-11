package remote

import co.touchlab.kampkit.ktor.network
import co.touchlab.stately.ensureNeverFrozen
import domain.data.IntradayData
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
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
//                    log("").v("Network") { message }
//                    log("")
                }
            }

            level = LogLevel.INFO
        }
    }

    init {
        ensureNeverFrozen()
    }


    override suspend fun getIntradayData(): IntradayData =
        network {
//            log.d { "Fetching Breeds from network" }
            client.get<IntradayData> {
                intraday("query?function=TIME_SERIES_INTRADAY&symbol=APPL&interval=5min&apikey= JVCK1O23CTQ7TPQM")
            }
        }

    private fun HttpRequestBuilder.intraday(path: String) {
        url {
            takeFrom("https://www.alphavantage.co/")
            encodedPath = path
        }
    }
}

//internal expect suspend fun <R> network(block: suspend () -> R): R