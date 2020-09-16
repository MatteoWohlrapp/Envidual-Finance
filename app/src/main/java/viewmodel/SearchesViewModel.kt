package viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.data.CompanyData
import domain.use_cases.*
import io.ktor.client.features.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.CompanyDataNotFoundException
import remote.NoInternetConnectionException

class SearchesViewModel() : ViewModel(), KoinComponent {
    private val getCompaniesForSearches: GetCompaniesForSearchesUseCase by inject()
    private val getCompanyByTicker: GetCompanyByTickerUseCase by inject()
    private val addCompanyToFavourites: AddCompanyToFavouritesUseCase by inject()
    private val deleteCompanyFromSearches: DeleteCompanyFromSearchesUseCase by inject()
    private val deleteCompanyFromFavourites: DeleteCompanyFromFavouritesUseCase by inject()

    var searches = MutableLiveData<List<CompanyData>>()
    var searchesProgressBar = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()


    fun getCompanyDataForSearches() {
        searchesProgressBar.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try{
                    val data = getCompaniesForSearches.invoke()
                    searchesProgressBar.postValue(false)
                    data.collect {
                        searches.postValue(it)
                    }
                } catch (e: java.lang.Exception){
                    searchesProgressBar.postValue(false)
                    handleException(e)
                }

            }
        }
    }

    fun getCompanyDataForSearchesWithTicker(ticker: String) {
        searchesProgressBar.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    getCompanyByTicker.invoke(ticker)
                } catch (e: Exception) {
                    handleException(e)
                } finally {
                    searchesProgressBar.postValue(false)
                }
            }
        }
    }

    fun addCompanyToFavourites(companyData: CompanyData) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                addCompanyToFavourites.invoke(companyData)
            }
        }
    }

    fun deleteCompanyFromFavourites(companyData: CompanyData) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deleteCompanyFromFavourites.invoke(companyData)
            }
        }
    }

    fun deleteCompanyFromSearches(companyData: CompanyData) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deleteCompanyFromSearches.invoke(companyData)
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
