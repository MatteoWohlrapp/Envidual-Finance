package viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.data.CompanyNews
import domain.use_cases.GetCompanyNewsByTickerUseCase
import io.ktor.client.features.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.CompanyDataNotFoundException
import remote.CompanyNewsNotFoundException

class CompanyDetailedViewModel : ViewModel(), KoinComponent {

    var companyNews = MutableLiveData<List<CompanyNews>>()
    var companyNewsProgressBar = MutableLiveData<Boolean>()
    var companyNewsNotFound = MutableLiveData<Boolean>()
    var toManyRequests = MutableLiveData<Boolean>()
    private val getCompanyNewsByTicker: GetCompanyNewsByTickerUseCase by inject()

    fun getCompanyNewsByTicker(ticker: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val companyNewsFromUseCase = getCompanyNewsByTicker.invoke(ticker)
                    companyNewsProgressBar.postValue(false)
                    companyNews.postValue(companyNewsFromUseCase)
                } catch (e: CompanyNewsNotFoundException) {
                    companyNewsNotFound.postValue(true)
                } catch (e: ClientRequestException){
                    toManyRequests.postValue(true)
                } finally {
                    companyNewsProgressBar.postValue(false)
                }
            }
        }
    }

}