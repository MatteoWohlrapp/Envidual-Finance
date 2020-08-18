package domain.use_cases

import co.example.envidual.finance.touchlab.db.Favourites
import domain.data.CompanyData
import remote.RemoteFinanceInterface
import org.koin.core.KoinComponent
import org.koin.core.inject
import sql.FavouritesDatabaseHelper
import sql.SearchesDatabaseHelper


class GetCompanyByTickerUseCase : KoinComponent {

    private val remoteFinance : RemoteFinanceInterface by inject()
    private val dbHelperFavourites : FavouritesDatabaseHelper by inject()
    private val dbHelperSearches : SearchesDatabaseHelper by inject()

    suspend fun invoke(ticker: String) : CompanyData {
        val upperCaseTicker = ticker.toUpperCase()
        var data: CompanyData
        println("About to access data")
        val companiesByTickerFromFavourites = dbHelperFavourites.selectByTickerFromFavourites(upperCaseTicker)
        val companiesByTickerFromSearches = dbHelperSearches.selectByTickerFromSearches(upperCaseTicker)
        println("ticker is $upperCaseTicker and the list from favourites contains $companiesByTickerFromFavourites")
        println("ticker is $upperCaseTicker and the list from searches contains $companiesByTickerFromSearches")

        if (companiesByTickerFromFavourites.isEmpty() && companiesByTickerFromSearches.isEmpty()) {
            // the company was not found in the database, we need to fetch from remote
            println("found no data for the ticker in both tables")
            data = remoteFinance.getCompanyData(upperCaseTicker)
            dbHelperSearches.insertSearches(listOf(data))
        } else if(companiesByTickerFromFavourites.isEmpty()) {
            println("found data for ticker in searches")

            val companyInfos = companiesByTickerFromSearches.first()
            data = CompanyData(
                companyInfos.country,
                companyInfos.currency,
                companyInfos.finnhubIndustry,
                companyInfos.ipo,
                companyInfos.logo,
                companyInfos.marketCapitalization!!.toFloat(),
                companyInfos.name,
                companyInfos.ticker
            )
        } else if(companiesByTickerFromSearches.isEmpty()){
            println("found data for ticker in favourites")


            val companyInfos = companiesByTickerFromFavourites.first()
            data = CompanyData(
                companyInfos.country,
                companyInfos.currency,
                companyInfos.finnhubIndustry,
                companyInfos.ipo,
                companyInfos.logo,
                companyInfos.marketCapitalization,
                companyInfos.name,
                companyInfos.ticker
            )

            dbHelperSearches.insertSearches(listOf(data))
        } else {
            println("found data for ticker in both tables")

            val companyInfos = companiesByTickerFromSearches.first()

            data = CompanyData(
                companyInfos.country,
                companyInfos.currency,
                companyInfos.finnhubIndustry,
                companyInfos.ipo,
                companyInfos.logo,
                companyInfos.marketCapitalization!!.toFloat(),
                companyInfos.name,
                companyInfos.ticker
            )
        }
        println(data.toString())
        return data
    }
}