package domain.data
import kotlinx.serialization.Serializable



@Serializable
data class CompanyData( var country: String? = null,
                        var currency: String? = null,
                        var finnhubIndustry: String? = null,
                        var ipo: String? = null,
                        var logo: String? = null,
                        var marketCapitalization: Float? = null,
                        var name: String? = null,
                        var ticker: String? = null,
                        var isFavourite: Boolean? = null,
                        var isSearched: Boolean? = null,
                        var lastSearched: Long? = null)