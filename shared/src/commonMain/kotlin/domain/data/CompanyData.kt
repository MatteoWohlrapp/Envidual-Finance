package domain.data
import kotlinx.serialization.Serializable



@Serializable
data class CompanyData(val country: String? = null,
                       val currency: String? = null,
                       val finnhubIndustry: String? = null,
                       val ipo: String? = null,
                       val logo: String? = null,
                       val marketCapitalization: Float? = null,
                       val name: String? = null,
                       val ticker: String? = null,
                       var checked: Boolean? = null)