package domain.use_cases

import cache.CompanyDataCacheInterface
import domain.data.CompanyData
import kotlinx.coroutines.flow.Flow
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.CompanyNotFoundException
import remote.RemoteFinanceInterface
import cache.DatabaseHelper

class GetCompaniesForFavouritesUseCase : KoinComponent {

    private val companyDataCache : CompanyDataCacheInterface by inject()
    private val remoteFinance: RemoteFinanceInterface by inject()

    private val defaultFavouriteCompaniesTicker =
        mutableListOf("MSFT", "AAPL", "AMZN", "FB", "GOOGL", "IBM")


    suspend fun invoke(): Flow<List<CompanyData>> {
        val data = companyDataCache.selectAllFavourites()
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
                    companiesFromRemote.add(company.copy(isFavourite = true, lastSearched = getTimestamp()))
                }
            }
            companyDataCache.insert(companiesFromRemote)
        }

        return companyDataCache.selectAllFavouritesAsFlow()
    }
}

expect fun getTimestamp(): Long