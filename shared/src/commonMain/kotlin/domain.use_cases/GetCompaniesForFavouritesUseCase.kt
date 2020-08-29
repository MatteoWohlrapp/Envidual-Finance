package domain.use_cases

import domain.data.CompanyData
import kotlinx.coroutines.flow.Flow
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.CompanyNotFoundException
import remote.RemoteFinanceInterface
import sql.DatabaseHelper

class GetCompaniesForFavouritesUseCase : KoinComponent {

    private val dbHelper: DatabaseHelper by inject()
    private val remoteFinance: RemoteFinanceInterface by inject()

    private val defaultFavouriteCompaniesTicker =
        mutableListOf("MSFT", "AAPL", "AMZN", "FB", "GOOGL", "IBM")


    suspend fun invoke(): Flow<List<CompanyData>> {
        val data = dbHelper.selectAllFavouritesFromCompanies()
        if(data.isEmpty()){
            val companiesFromRemote = mutableListOf<CompanyData>()
            for (ticker in defaultFavouriteCompaniesTicker) {
                var company = CompanyData()
                try {
                    company = remoteFinance.getCompanyData(ticker)
                } catch(e: CompanyNotFoundException){
                    println(e.toString())
                    }
                if (company.name != null) {
                    company.isFavourite = true
                    company.lastSearched = getTimestamp()
                    companiesFromRemote.add(company)
                }
            }

            dbHelper.insertCompanies(companiesFromRemote)
        }

        return dbHelper.selectAllFavouritesAsFlowFromCompanies()
    }
}

expect fun getTimestamp(): Long