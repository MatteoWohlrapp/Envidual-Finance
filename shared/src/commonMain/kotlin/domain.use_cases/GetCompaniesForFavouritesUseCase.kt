package domain.use_cases

import cache.CompanyDataCacheInterface
import domain.data.CompanyData
import kotlinx.coroutines.flow.Flow
import remote.CompanyDataNotFoundException
import remote.RemoteFinanceInterface
import co.touchlab.stately.freeze
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext
import remote.NoInternetConnectionException

class GetCompaniesForFavouritesUseCase(
    private val companyDataCache: CompanyDataCacheInterface,
    private val remoteFinance: RemoteFinanceInterface
) {

    private val defaultFavouriteCompaniesTicker =
        mutableListOf("MSFT", "AAPL", "AMZN", "FB", "GOOGL", "IBM")


    @Throws(Exception::class, NoInternetConnectionException::class)
    suspend fun invoke(): Flow<List<CompanyData>> {
        return withContext(backgroundDispatcher) {
            val data = companyDataCache.selectAllFavourites()
            if (data.isEmpty()) {
                val companiesFromRemote = mutableListOf<CompanyData>()
                for (ticker in defaultFavouriteCompaniesTicker) {
                    var company = CompanyData()
                    try {
                        company = remoteFinance.getCompanyData(ticker)
                    } catch (e: CompanyDataNotFoundException) {
                        println(e.toString())
                    }
                    if (company.name != null) {
                        companiesFromRemote.add(
                            company.copy(
                                isFavourite = true,
                                lastSearched = getTimestamp()
                            )
                        )
                    }
                }
                companyDataCache.insert(companiesFromRemote)
            }

            return@withContext companyDataCache.selectAllFavouritesAsFlow()
        }
    }
}

expect fun getTimestamp(): Long