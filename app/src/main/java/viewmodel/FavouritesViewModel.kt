package viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.data.CompanyData
import domain.use_cases.GetCompaniesForFavouritesUseCase
import domain.use_cases.DeleteCompanyFromFavouritesUseCase
import domain.use_cases.UpdateCompaniesUseCase
import io.ktor.client.features.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.CompanyDataNotFoundException
import remote.NoInternetConnectionException

class FavouritesViewModel() : ViewModel(), KoinComponent{

    var favourites = MutableLiveData<List<CompanyData>>()
    var favouritesProgressBar = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()

    private val getCompaniesForFavourites: GetCompaniesForFavouritesUseCase by inject()
    private val deleteCompanyFromFavourites: DeleteCompanyFromFavouritesUseCase by inject()
    private val updateCompanies: UpdateCompaniesUseCase by inject()

    fun getCompanyDataForFavourites(){
        
        favouritesProgressBar.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val data = getCompaniesForFavourites.invoke()
                    favouritesProgressBar.postValue(false)
                    data.collect { favourites.postValue(it) }
                } catch (e: Exception){
                    favouritesProgressBar.postValue(false)
                    handleException(e)
                }

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

    fun updateCompanies(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try{
                    updateCompanies.invoke()
                } catch (e:Exception){
                    handleException(e)
                }
            }
        }
    }

    private fun handleException(e:Exception){
        when (e) {
            is ClientRequestException -> errorMessage.postValue(e.message)
            is CompanyDataNotFoundException -> errorMessage.postValue(e.message)
            is NoInternetConnectionException -> errorMessage.postValue(e.message)
            else -> throw e
        }
    }
}
