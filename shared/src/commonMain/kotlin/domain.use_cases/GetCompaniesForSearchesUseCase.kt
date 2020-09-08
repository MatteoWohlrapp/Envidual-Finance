package domain.use_cases

import cache.CompanyDataCacheInterface
import domain.data.CompanyData
import kotlinx.coroutines.flow.Flow
import org.koin.core.KoinComponent
import org.koin.core.inject
import cache.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetCompaniesForSearchesUseCase(private val companyDataCache : CompanyDataCacheInterface) {

    suspend fun invoke(): Flow<List<CompanyData>> = withContext(backgroundDispatcher){
        companyDataCache.selectAllSearchesAsFlow()
    }
}
