package domain.use_cases

import cache.CompanyDataCacheInterface
import domain.data.CompanyData
import org.koin.core.KoinComponent
import org.koin.core.inject
import cache.DatabaseHelper

class DeleteCompanyFromSearchesUseCase(private val companyDataCache : CompanyDataCacheInterface){

    suspend fun invoke(companyData: CompanyData){
        companyDataCache.updateIsSearchedByTicker(false, companyData.ticker!!)
    }

}