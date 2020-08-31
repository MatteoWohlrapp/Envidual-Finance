package domain.use_cases

import cache.CompanyDataCacheInterface
import domain.data.CompanyData
import org.koin.core.KoinComponent
import org.koin.core.inject
import cache.DatabaseHelper

class DeleteCompanyFromSearchesUseCase: KoinComponent {

    private val companyDataCache : CompanyDataCacheInterface by inject()

    suspend fun invoke(companyData: CompanyData){
        companyData.isSearched = false
        companyDataCache.updateIsSearchedByTicker(companyData.isSearched!!, companyData.ticker!!)
    }

}