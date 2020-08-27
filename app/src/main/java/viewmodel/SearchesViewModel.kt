package viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.data.CompanyData
import domain.use_cases.AddCompanyToFavouritesUseCase
import domain.use_cases.GetCompaniesForSearchesUseCase
import domain.use_cases.GetCompanyByTickerUseCase
import domain.use_cases.DeleteCompanyFromFavouritesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.NoCompanyFoundException

class SearchesViewModel : ViewModel(), KoinComponent {


    private val getCompaniesForSearches : GetCompaniesForSearchesUseCase by inject()
    private val getCompanyByTicker : GetCompanyByTickerUseCase by inject()
    private val addCompanyToFavourites: AddCompanyToFavouritesUseCase by inject()
    private val deleteCompanyFromFavourites: DeleteCompanyFromFavouritesUseCase by inject()
    var searches = MutableLiveData<List<CompanyData>>()
    var searchesProgressBar = MutableLiveData<Boolean>()


    fun getCompanyDataForSearches(){
        searchesProgressBar.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val data = getCompaniesForSearches.invoke()
                searchesProgressBar.postValue(false)
                data.collect { searches.postValue(it) }
            }
        }
    }

    fun getCompanyDataForSearchesWithTicker(ticker:String){
        searchesProgressBar.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                var data = CompanyData()
                try {
                    Log.d("Searches", ticker)
                    data = getCompanyByTicker.invoke(ticker)
                    Log.d("Searches", data.name)

                } catch(e: NoCompanyFoundException){
                    Log.d("Searches", "Company not found")
                }

                searchesProgressBar.postValue(false)
                searches.postValue(listOf(data))
            }
        }
    }

    fun addCompanyToFavourites(companyData: CompanyData){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                addCompanyToFavourites.invoke(companyData)
            }
        }
    }

    fun removeCompanyFromFavourites(companyData: CompanyData){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                deleteCompanyFromFavourites.invoke(companyData)
            }
        }
    }

}