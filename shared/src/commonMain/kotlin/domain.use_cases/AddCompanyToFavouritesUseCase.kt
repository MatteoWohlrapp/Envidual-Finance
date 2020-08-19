package domain.use_cases

import domain.data.CompanyData
import org.koin.core.KoinComponent
import org.koin.core.inject
import sql.DatabaseHelper

class AddCompanyToFavouritesUseCase : KoinComponent{

    private val dbHelper: DatabaseHelper by inject()


    suspend fun invoke(companyData: CompanyData){
        companyData.isFavourite = true
        dbHelper.changeIsFavouriteForTicker(companyData.isFavourite!!, companyData.ticker!!)

//        val data = dbHelperFavourites.selectByTickerFromFavourites(companyData.ticker!!)
//        if(data.isEmpty())
//            dbHelperFavourites.insertFavourites(listOf(companyData))
    }
}