package viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.data.CompanyData
import domain.use_cases.GetCompaniesForFavouritesUseCase
import domain.use_cases.DeleteCompanyFromFavouritesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

class FavouritesViewModel() : ViewModel(), KoinComponent{

    var favourites = MutableLiveData<List<CompanyData>>()
    val getCompaniesForFavourites: GetCompaniesForFavouritesUseCase by inject()
    val deleteCompanyFromFavourites: DeleteCompanyFromFavouritesUseCase by inject()

    fun getCompanyDataForFavourites(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val data = getCompaniesForFavourites.invoke()
                data.collect { favourites.postValue(it) }
            }
        }
    }

    fun deleteCompanyFromFavourites(companyData: CompanyData){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                deleteCompanyFromFavourites.invoke(companyData)
            }
        }
    }
}
