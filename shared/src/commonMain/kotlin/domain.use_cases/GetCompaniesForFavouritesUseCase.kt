package domain.use_cases

import domain.data.CompanyData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.RemoteFinanceInterface
import sql.DatabaseHelper

class GetCompaniesForFavouritesUseCase : KoinComponent {

    private val dbHelper: DatabaseHelper by inject()
    private val remoteFinance: RemoteFinanceInterface by inject()

    private val defaultFavouriteCompaniesTicker =
        mutableListOf("MSFT", "AAPL", "AMZN", "FB", "GOOGL", "IBM", "BRK.B")


    suspend fun invoke(): Flow<List<CompanyData>> {
        val data = dbHelper.selectAllFavourites()
        if(data.isEmpty()){
            val exploreList = mutableListOf<CompanyData>()
            for (ticker in defaultFavouriteCompaniesTicker) {
                val data = remoteFinance.getCompanyData(ticker)
                if (data.name != null) {
                    data.isFavourite = true
                    exploreList.add(data)
                }
            }
            dbHelper.insertCompany(exploreList)
        }
        return dbHelper.selectAllFavouritesAsFlow().map { it ->
            val companies = mutableListOf<CompanyData>()

            for(company in it){
                companies.add(CompanyData(company.country, company.currency, company.finnhubIndustry, company.ipo,
                    company.logo, company.marketCapitalization, company.name, company.ticker, company.isFavourite))
            }

            companies
        }
    }
}