package domain.use_cases

import domain.data.CompanyData
import org.koin.core.KoinComponent
import org.koin.core.inject
import sql.FavouritesDatabaseHelper

class RemoveCompanyFromFavouritesUseCase: KoinComponent {

    private val dbHelperFavourites : FavouritesDatabaseHelper by inject()


    suspend fun invoke(companyData: CompanyData){
        dbHelperFavourites.deleteCompanyFromFavourites(companyData.name!!)
    }

}