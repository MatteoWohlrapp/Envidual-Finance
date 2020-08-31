package domain.use_cases

import cache.CompanyDataCacheInterface
import cache.CompanyNewsCacheInterface
import domain.data.CompanyData
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.RemoteFinanceInterface
import cache.DatabaseHelper

class UpdateCompaniesUseCase: KoinComponent {

    private val companyDataCache : CompanyDataCacheInterface by inject()
    private val remoteFinance: RemoteFinanceInterface by inject()

    suspend fun invoke(){
        val companiesToUpdate = companyDataCache.selectCompaniesToUpdate(getTimestamp()-86400)

        val updatedCompanies = mutableListOf<CompanyData>()

        for(company in companiesToUpdate){
            val time = getTimestamp()
            val companyData = remoteFinance.getCompanyData(company.ticker!!)

            companyData.lastSearched = time
            companyData.isSearched = company.isSearched
            companyData.isFavourite = company.isFavourite

            updatedCompanies.add(companyData)
        }
        companyDataCache.insert(updatedCompanies)
    }
}

