package domain.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

@Serializable
data class IntradayData(@SerialName("Meta Data") val metaData : MetaData) {
}

@Serializable
data class MetaData(
    @SerialName("1. Information") val info: String,
    @SerialName("2. Symbol") val symbol: String
)