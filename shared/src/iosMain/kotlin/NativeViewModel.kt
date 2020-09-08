package co.touchlab.kampkit

import co.touchlab.stately.ensureNeverFrozen
import domain.data.CompanyData
import domain.data.CompanyNews
import domain.use_cases.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.CompanyNotFoundException


class NativeViewModel(
    private val viewUpdate: (List<CompanyData>) -> Unit,
    private val newsUpdate: (List<CompanyNews>) -> Unit,
    private val errorUpdate: (String) -> Unit
) : KoinComponent {

    private val scope = MainScope(Dispatchers.Main)
    private val getCompanyByTickerUseCase: GetCompanyByTickerUseCase by inject()
    private val getCompaniesForFavouritesUseCase: GetCompaniesForFavouritesUseCase by inject()
    private val getCompaniesForSearchesUseCase: GetCompaniesForSearchesUseCase by inject()
    private val addCompanyToFavouritesUseCase: AddCompanyToFavouritesUseCase by inject()
    private val deleteCompanyFromFavouritesUseCase: DeleteCompanyFromFavouritesUseCase by inject()
    private val deleteCompanyFromSearchesUseCase: DeleteCompanyFromSearchesUseCase by inject()
    private val getCompanyNewsByTickerUseCase: GetCompanyNewsByTickerUseCase by inject()
    private val updateCompaniesUseCase: UpdateCompaniesUseCase by inject()


    init {
        ensureNeverFrozen()
    }

    fun getCompanyByTicker(ticker:String) {
        scope.launch {
            try {
                getCompanyByTickerUseCase.invoke(ticker)
            }
            catch(e: Throwable) {
                println(e.message)
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
