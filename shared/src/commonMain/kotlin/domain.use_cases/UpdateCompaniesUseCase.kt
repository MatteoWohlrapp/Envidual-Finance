package domain.use_cases

import cache.CompanyDataCacheInterface
import cache.CompanyNewsCacheInterface
import domain.data.CompanyData
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.RemoteFinanceInterface
import cache.DatabaseHelper
import co.touchlab.stately.freeze
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateCompaniesUseCase(
    private val companyDataCache: CompanyDataCacheInterface,
    private val remoteFinance: RemoteFinanceInterface
) {


    suspend fun invoke() {
//        try {
//            remoteFinance.freeze()
//        } catch (e: Throwable) {
//            println(e.message)
//        }

        withContext(mainDispatcher) {
            val companiesToUpdate = companyDataCache.selectCompaniesToUpdate(getTimestamp() - 86400)

            val updatedCompanies = mutableListOf<CompanyData>()

            for (company in companiesToUpdate) {
                val time = getTimestamp()
//                try {
//                    remoteFinance.freeze()
//                } catch (e: Throwable) {
//                    println(e.message)
//                }
                val companyData = remoteFinance.getCompanyData(company.ticker!!)
                val modifiedCompanyData = companyData.copy(
                    lastSearched = time, isSearched = company.isSearched,
                    isFavourite = company.isFavourite
                )
                updatedCompanies.add(modifiedCompanyData)
            }
            companyDataCache.insert(updatedCompanies)
        }
    }
}

