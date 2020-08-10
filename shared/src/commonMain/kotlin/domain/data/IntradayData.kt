package domain.data

import kotlinx.serialization.Serializable

@Serializable
data class IntradayData(val metaData : Map<String, String>) {
}