package domain.use_cases

import domain.data.CompanyData
import org.koin.core.KoinComponent
import org.koin.core.inject
import sql.DatabaseHelper

class AddCompanyToFavouritesUseCase : KoinComponent{

    private val dbHelper: DatabaseHelper by inject()


    suspend fun invoke(companyData: CompanyData){
//        val data = dbHelper.selectByTicker(companyData.ticker!!).first()
//        dbHelper.insertCompany(listOf(CompanyData(data.country, data.currency, data.finnhubIndustry, data.ipo, data.logo, data.marketCapitalization,
//        data.name, data.ticker, true, data.isSearched, data.lastSearched)))
        companyData.isFavourite = true
        dbHelper.changeIsFavouriteForTicker(companyData.isFavourite!!, companyData.ticker!!)
    }
}