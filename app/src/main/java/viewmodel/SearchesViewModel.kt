package viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.data.CompanyData
import domain.use_cases.AddCompanyToFavouritesUseCase
import domain.use_cases.GetCompaniesForSearchesUseCase
import domain.use_cases.GetCompanyByTickerUseCase
import domain.use_cases.RemoveCompanyFromFavouritesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

class SearchesViewModel : ViewModel(), KoinComponent {


    private val getCompaniesForSearches : GetCompaniesForSearchesUseCase by inject()
    private val getCompanyByTicker : GetCompanyByTickerUseCase by inject()
    private val addCompanyToFavourites: AddCompanyToFavouritesUseCase by inject()
    private val removeCompanyFromFavourites: RemoveCompanyFromFavouritesUseCase by inject()
    var searches = MutableLiveData<List<CompanyData>>()


    fun getCompanyDataForSearches(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val data = getCompaniesForSearches.invoke()
                data.collect { searches.postValue(it) }
            }
        }
    }

    fun getCompanyDataForSearchesWithTicker(ticker:String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val data = getCompanyByTicker.invoke(ticker)
                Log.d("Searches", data.name)
//                searches.postValue(listOf())
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
                removeCompanyFromFavourites.invoke(companyData)
            }
        }
    }

}