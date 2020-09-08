package domain.use_cases

import cache.CompanyDataCacheInterface
import domain.data.CompanyData
import org.koin.core.KoinComponent
import org.koin.core.inject
import cache.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddCompanyToFavouritesUseCase(private val companyDataCache: CompanyDataCacheInterface) {

    suspend fun invoke(companyData: CompanyData) {
        withContext(backgroundDispatcher) {
            companyDataCache.updateIsFavouriteByTicker(true, companyData.ticker!!)
        }
    }
}