package viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.data.CompanyData
import domain.use_cases.GetCompaniesForSearchesUseCase
import domain.use_cases.GetCompanyByTickerUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

class SearchesViewModel : ViewModel(), KoinComponent {


    val getCompaniesForSearches : GetCompaniesForSearchesUseCase by inject()
    val getCompanyByTickerUseCase : GetCompanyByTickerUseCase by inject()
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
                val data = getCompanyByTickerUseCase.invoke(ticker)
                searches.postValue(listOf(data))
            }
        }
    }

    fun addCompanyToFavourites(companyData: CompanyData){

    }

}