package co.touchlab.kampkit

import co.touchlab.stately.ensureNeverFrozen
import domain.data.CompanyData
import domain.use_cases.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import remote.NoCompanyFoundException


class NativeViewModel(
    private val viewUpdate: (List<CompanyData>) -> Unit,
    private val errorUpdate: (String) -> Unit
) : KoinComponent {

    private val scope = MainScope(Dispatchers.Main)
    private val getCompanyByTickerUseCase: GetCompanyByTickerUseCase
    private val getCompaniesForFavouritesUseCase: GetCompaniesForFavouritesUseCase
    private val getCompaniesForSearchesUseCase: GetCompaniesForSearchesUseCase
    private val addCompanyToFavouritesUseCase : AddCompanyToFavouritesUseCase
    private val deleteCompanyFromFavouritesUseCase : DeleteCompanyFromFavouritesUseCase
    private val deleteCompanyFromSearchesUseCase: DeleteCompanyFromSearchesUseCase


    init {
        ensureNeverFrozen()
        getCompanyByTickerUseCase = GetCompanyByTickerUseCase()
        getCompaniesForFavouritesUseCase = GetCompaniesForFavouritesUseCase()
        getCompaniesForSearchesUseCase = GetCompaniesForSearchesUseCase()
        addCompanyToFavouritesUseCase = AddCompanyToFavouritesUseCase()
        deleteCompanyFromFavouritesUseCase = DeleteCompanyFromFavouritesUseCase()
        deleteCompanyFromSearchesUseCase = DeleteCompanyFromSearchesUseCase()
    }

    fun getCompanyByTicker(ticker:String) {
        scope.launch {
            try {
                getCompanyByTickerUseCase.invoke(ticker)
            }
            catch(e: NoCompanyFoundException) {
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



    fun onDestroy() {
        scope.onDestroy()
    }
}
