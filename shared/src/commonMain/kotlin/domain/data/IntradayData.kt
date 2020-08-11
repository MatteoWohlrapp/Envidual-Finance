package domain.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

@Serializable
data class IntradayData(@SerialName("Meta Data") val metaData : HashMap<String, String>,
@SerialName("Time Series (5min)")val timeData : HashMap<String, HashMap<String, String>>) {
}
