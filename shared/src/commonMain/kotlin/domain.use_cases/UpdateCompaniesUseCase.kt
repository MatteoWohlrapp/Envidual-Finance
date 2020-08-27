package domain.use_cases

import domain.data.CompanyData
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.RemoteFinanceInterface
import sql.DatabaseHelper

class UpdateCompaniesUseCase: KoinComponent {

    private val dbHelper: DatabaseHelper by inject()
    private val remoteFinance: RemoteFinanceInterface by inject()

    suspend fun invoke(){
        val companiesToUpdate = dbHelper.selectCompaniesToUpdate(getTimestamp()-86400)

        for(company in companiesToUpdate){
            val time = getTimestamp()
            val data = remoteFinance.getCompanyData(company.ticker)
            dbHelper.insertCompany(listOf(CompanyData(data.country, data.currency, data.finnhubIndustry, data.ipo,
            data.logo, data.marketCapitalization, data.name, data.ticker, company.isFavourite, company.isSearched, time)), time)
        }
    }
}

