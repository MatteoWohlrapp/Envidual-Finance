package domain.use_cases

import domain.data.CompanyData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.KoinComponent
import org.koin.core.inject
import sql.SearchesDatabaseHelper

class GetCompaniesForSearchesUseCase: KoinComponent {

    private val searchesDatabaseHelper: SearchesDatabaseHelper by inject()

    suspend fun invoke(): Flow<List<CompanyData>> =
         searchesDatabaseHelper.selectAllItemsFromSearchAsFlow().map { it ->
            val companies = mutableListOf<CompanyData>()

            for(search in it){
                companies.add(CompanyData(search.country, search.currency, search.finnhubIndustry, search.ipo,
                search.logo, search.marketCapitalization, search.name, search.ticker))
            }

            companies
        }

}
