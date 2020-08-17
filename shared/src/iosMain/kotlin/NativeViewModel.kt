package co.touchlab.kampkit

import co.touchlab.stately.ensureNeverFrozen
import domain.data.CompanyData
import domain.use_cases.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent


class NativeViewModel(
    private val viewUpdate: (List<CompanyData>) -> Unit,
    private val errorUpdate: (String) -> Unit
) : KoinComponent {

    private val scope = MainScope(Dispatchers.Main)
    private var getCompanyByTickerUseCase: GetCompanyByTickerUseCase
    private var getCompaniesForFavouritesUseCase: GetCompaniesForFavouritesUseCase
    private var getCompaniesForSearchesUseCase: GetCompaniesForSearchesUseCase
    private var addCompanyToFavouritesUseCase : AddCompanyToFavouritesUseCase
    private var removeCompanyFromFavouritesUseCase : RemoveCompanyFromFavouritesUseCase


    init {
        ensureNeverFrozen()
        getCompanyByTickerUseCase = GetCompanyByTickerUseCase()
        getCompaniesForFavouritesUseCase = GetCompaniesForFavouritesUseCase()
        getCompaniesForSearchesUseCase = GetCompaniesForSearchesUseCase()
        addCompanyToFavouritesUseCase = AddCompanyToFavouritesUseCase()
        removeCompanyFromFavouritesUseCase = RemoveCompanyFromFavouritesUseCase()
    }

    fun getCompanyByTicker(ticker:String) {
        scope.launch {
            getCompanyByTickerUseCase.invoke(ticker)
        }
    }

    fun startObservingFavorites() {
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

    fun addFavorite(company: CompanyData) {
        scope.launch {
            addCompanyToFavouritesUseCase.invoke(company)
        }
    }

    fun removeFavorite(company: CompanyData) {
        scope.launch {
            removeCompanyFromFavouritesUseCase.invoke(company)
        }
    }



    fun onDestroy() {
        scope.onDestroy()
    }
}
