package domain.use_cases

import domain.data.CompanyData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.RemoteFinanceInterface
import sql.FavouritesDatabaseHelper

class GetCompaniesForFavouritesUseCase : KoinComponent {

    private val remoteFinance: RemoteFinanceInterface by inject()
    private val favouritesDatabaseHelper: FavouritesDatabaseHelper by inject()

    private val exploreCompaniesTicker =
        mutableListOf("MSFT", "AAPL", "AMZN", "FB", "GOOGL", "IBM", "BRK.B")


    suspend fun invoke(): Flow<List<CompanyData>> {
        val data = favouritesDatabaseHelper.selectAllItemsFromFavourites()
        if(data.isEmpty()){
            val exploreList = mutableListOf<CompanyData>()
            for (ticker in exploreCompaniesTicker) {
                val data = remoteFinance.getCompanyData(ticker)
                if (data.name != null)
                    exploreList.add(data)
            }
            favouritesDatabaseHelper.insertFavourites(exploreList)
        }
        return favouritesDatabaseHelper.selectAllItemsFromFavouritesAsFlow().map { it ->
            val companies = mutableListOf<CompanyData>()

            for(search in it){
                companies.add(CompanyData(search.country, search.currency, search.finnhubIndustry, search.ipo,
                    search.logo, search.marketCapitalization, search.name, search.ticker))
            }

            companies
        }
    }
}