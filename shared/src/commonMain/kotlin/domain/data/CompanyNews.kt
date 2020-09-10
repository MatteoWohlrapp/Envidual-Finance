package domain.data
import kotlinx.serialization.Serializable



@Serializable
data class CompanyNews(
    var ticker: String? = null,
    var category: String? = null,
    var datetime: Long? = null,
    var headline: String? = null,
    var id: Long? = null,
    var image: String? = null,
    var related: String? = null,
    var source: String? = null,
    var summary: String? = null,
    var url: String? = null,
    var lastRequested: Long? = null
)