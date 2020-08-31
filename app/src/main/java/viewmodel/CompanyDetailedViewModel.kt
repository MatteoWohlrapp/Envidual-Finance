package viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.data.CompanyNews
import domain.use_cases.GetCompanyNewsByTickerUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.CompanyNotFoundException

class CompanyDetailedViewModel : ViewModel(), KoinComponent{

    var companyNews = MutableLiveData<List<CompanyNews>>()
    var companyNewsProgressBar = MutableLiveData<Boolean>()
    var companyNewsNotFound = MutableLiveData<Boolean>()
    val getCompanyNewsByTicker : GetCompanyNewsByTickerUseCase by inject()

    fun getCompanyNewsByTicker(ticker:String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try{
                    val companyNewsAsFlow = getCompanyNewsByTicker.invoke(ticker)
                    companyNewsAsFlow.collect {
                        companyNewsProgressBar.postValue(false)
                        companyNews.postValue(it) }
                } catch(e: CompanyNotFoundException){
                    companyNewsNotFound.postValue(true)
                } finally {
                    companyNewsProgressBar.postValue(false)
                }
            }
        }
    }

}