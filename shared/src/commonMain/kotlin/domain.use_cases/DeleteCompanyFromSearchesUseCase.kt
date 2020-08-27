package domain.use_cases

import domain.data.CompanyData
import org.koin.core.KoinComponent
import org.koin.core.inject
import sql.DatabaseHelper

class DeleteCompanyFromSearchesUseCase: KoinComponent {

    private val dbHelper: DatabaseHelper by inject()

    suspend fun invoke(companyData: CompanyData){
        companyData.isSearched = false
        dbHelper.changeIsSearchedForTicker(companyData.isSearched!!, companyData.ticker!!)

    }

}