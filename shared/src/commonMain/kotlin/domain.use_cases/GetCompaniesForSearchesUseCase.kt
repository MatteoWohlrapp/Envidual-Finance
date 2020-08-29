package domain.use_cases

import domain.data.CompanyData
import kotlinx.coroutines.flow.Flow
import org.koin.core.KoinComponent
import org.koin.core.inject
import sql.DatabaseHelper

class GetCompaniesForSearchesUseCase: KoinComponent {

    private val dbHelper: DatabaseHelper by inject()

    fun invoke(): Flow<List<CompanyData>> =
         dbHelper.selectAllSearchesAsFlowFromCompanies()
}
