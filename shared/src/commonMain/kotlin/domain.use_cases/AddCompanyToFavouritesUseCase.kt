package domain.use_cases

import cache.CompanyDataCacheInterface
import domain.data.CompanyData
import org.koin.core.KoinComponent
import org.koin.core.inject
import cache.DatabaseHelper
import kotlinx.coroutines.withContext

class AddCompanyToFavouritesUseCase : KoinComponent{

    private val companyDataCache : CompanyDataCacheInterface by inject()

    suspend fun invoke(companyData: CompanyData){
        companyDataCache.updateIsFavouriteByTicker(true, companyData.ticker!!)
    }
}