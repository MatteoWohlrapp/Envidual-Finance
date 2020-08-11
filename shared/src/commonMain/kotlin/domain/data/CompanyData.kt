package domain.data
import kotlinx.serialization.Serializable



@Serializable
data class CompanyData(val country: String, val currency: String, val finnhubIndustry: String,
                   val ipo: String, val logo: String, val marketCapitalization: Int, val name: String,
                    val ticker: String)