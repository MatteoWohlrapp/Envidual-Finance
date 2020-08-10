package remote

import domain.data.IntradayData
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging

class FinanceRemote : RemoteFinanceInterface{

    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
//                    log("").v("Network") { message }
//                    log("").
                }
            }

            level = LogLevel.INFO
        }
    }


    override suspend fun getJsonFromApi(): IntradayData {
        TODO("Not yet implemented")
    }



}