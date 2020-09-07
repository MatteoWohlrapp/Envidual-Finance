package domain.use_cases

import cache.CompanyDataCacheInterface
import domain.data.CompanyData
import org.koin.core.KoinComponent
import org.koin.core.inject
import cache.DatabaseHelper

class DeleteCompanyFromFavouritesUseCase(private val companyDataCache : CompanyDataCacheInterface) {

    suspend fun invoke(companyData: CompanyData){
        companyDataCache.updateIsFavouriteByTicker(false, companyData.ticker!!)
    }

}