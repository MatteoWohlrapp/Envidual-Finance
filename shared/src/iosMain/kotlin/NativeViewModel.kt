package co.touchlab.kampkit

import co.touchlab.stately.ensureNeverFrozen
import domain.data.CompanyData
import domain.data.CompanyNews
import domain.use_cases.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import remote.CompanyNotFoundException


class NativeViewModel(
    private val viewUpdate: (List<CompanyData>) -> Unit,
    private val newsUpdate: (List<CompanyNews>) -> Unit,
    private val errorUpdate: (String) -> Unit
) : KoinComponent {

    private val scope = MainScope(Dispatchers.Main)
    private val getCompanyByTickerUseCase = GetCompanyByTickerUseCase()
    private val getCompaniesForFavouritesUseCase = GetCompaniesForFavouritesUseCase()
    private val getCompaniesForSearchesUseCase = GetCompaniesForSearchesUseCase()
    private val addCompanyToFavouritesUseCase = AddCompanyToFavouritesUseCase()
    private val deleteCompanyFromFavouritesUseCase = DeleteCompanyFromFavouritesUseCase()
    private val deleteCompanyFromSearchesUseCase = DeleteCompanyFromSearchesUseCase()
    private val getCompanyNewsByTickerUseCase = GetCompanyNewsByTickerUseCase()
    private val updateCompaniesUseCase = UpdateCompaniesUseCase()


    init {
        ensureNeverFrozen()
    }

    fun getCompanyByTicker(ticker:String) {
        scope.launch {
            try {
                getCompanyByTickerUseCase.invoke(ticker)
            }
            catch(e: CompanyNotFoundException) {
                errorUpdate(e.message!!)
            }
        }
    }

    fun startObservingFavourites() {
        scope.launch {
            val companyData = getCompaniesForFavouritesUseCase.invoke()
            companyData.collect {
                viewUpdate(it)
            }
        }
    }

    fun startObservingSearches() {
        scope.launch {
            val companyData = getCompaniesForSearchesUseCase.invoke()
            companyData.collect {
                viewUpdate(it)
            }
        }
    }

    fun addFavourite(company: CompanyData) {
        scope.launch {
            addCompanyToFavouritesUseCase.invoke(company)
        }
    }

    fun removeFavourite(company: CompanyData) {
        scope.launch {
            deleteCompanyFromFavouritesUseCase.invoke(company)
        }
    }

    fun removeCompanyFromSearches(company: CompanyData) {
        scope.launch {
            deleteCompanyFromSearchesUseCase.invoke(company)
        }
    }

    fun startObservingNewsFor(ticker: String) {
        scope.launch {
            val news = getCompanyNewsByTickerUseCase.invoke(ticker)
            news.collect {
                newsUpdate(it)
            }
        }
    }

    fun updateCompanies() {
        scope.launch {
            updateCompaniesUseCase.invoke()
        }
    }



    fun onDestroy() {
        scope.onDestroy()
    }
}
