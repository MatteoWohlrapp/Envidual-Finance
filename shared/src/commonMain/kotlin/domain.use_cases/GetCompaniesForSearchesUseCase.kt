package domain.use_cases

import domain.data.CompanyData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.KoinComponent
import org.koin.core.inject
import sql.DatabaseHelper

class GetCompaniesForSearchesUseCase: KoinComponent {

    private val dbHelper: DatabaseHelper by inject()

    suspend fun invoke(): Flow<List<CompanyData>> =
         dbHelper.selectAllCompaniesAsFlow().map { it ->
            val companies = mutableListOf<CompanyData>()

            for(company in it){
                companies.add(CompanyData(company.country, company.currency, company.finnhubIndustry, company.ipo,
                company.logo, company.marketCapitalization, company.name, company.ticker, company.isFavourite))
            }

            companies
        }

}
