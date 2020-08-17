package domain.use_cases

import domain.data.CompanyData
import org.koin.core.KoinComponent
import org.koin.core.inject
import sql.FavouritesDatabaseHelper
import sql.SearchesDatabaseHelper

class AddCompanyToFavouritesUseCase : KoinComponent{

    private val dbHelperFavourites : FavouritesDatabaseHelper by inject()
    private val dbHelperSearches : SearchesDatabaseHelper by inject()


    suspend fun invoke(companyData: CompanyData){
        companyData.checked = true
        dbHelperSearches.changeCheckedForName(companyData.checked!!, companyData.name!!)
        dbHelperFavourites.insertFavourites(listOf(companyData))
    }
}