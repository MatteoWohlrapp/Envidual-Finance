package co.touchlab.kampkit

import co.touchlab.stately.ensureNeverFrozen
import domain.use_cases.*
import org.koin.core.KoinComponent
import org.koin.core.inject


class UseCases(): KoinComponent {

    val getCompanyByTickerUseCase: GetCompanyByTickerUseCase by inject()
    val getCompaniesForFavouritesUseCase: GetCompaniesForFavouritesUseCase by inject()
    val getCompaniesForSearchesUseCase: GetCompaniesForSearchesUseCase by inject()
    val addCompanyToFavouritesUseCase: AddCompanyToFavouritesUseCase by inject()
    val deleteCompanyFromFavouritesUseCase: DeleteCompanyFromFavouritesUseCase by inject()
    val deleteCompanyFromSearchesUseCase: DeleteCompanyFromSearchesUseCase by inject()
    val getCompanyNewsByTickerUseCase: GetCompanyNewsByTickerUseCase by inject()
    val updateCompaniesUseCase: UpdateCompaniesUseCase by inject()

    init {
        ensureNeverFrozen()
    }

//    fun getCompanyByTicker(ticker:String) {
//        scope.launch {
//            try {
//                getCompanyByTickerUseCase.invoke(ticker)
//            }
//            catch(e: Throwable) {
//                println(e.message)
//                errorUpdate(e.message!!)
//            }
//        }
//    }
//
//    fun startObservingFavourites() {
//        scope.launch {
//            val companyData = getCompaniesForFavouritesUseCase.invoke()
//            companyData.collect {
//                viewUpdate(it)
//            }
//        }
//    }
//
//    fun startObservingSearches() {
//        scope.launch {
//            val companyData = getCompaniesForSearchesUseCase.invoke()
//            companyData.collect {
//                viewUpdate(it)
//            }
//        }
//    }
//
//    fun addFavourite(company: CompanyData) {
//        scope.launch {
//            addCompanyToFavouritesUseCase.invoke(company)
//        }
//    }
//
//    fun removeFavourite(company: CompanyData) {
//        scope.launch {
//            deleteCompanyFromFavouritesUseCase.invoke(company)
//        }
//    }
//
//    fun removeCompanyFromSearches(company: CompanyData) {
//        scope.launch {
//            deleteCompanyFromSearchesUseCase.invoke(company)
//        }
//    }
//
//    fun startObservingNewsFor(ticker: String) {
//        scope.launch {
//            val news = getCompanyNewsByTickerUseCase.invoke(ticker)
//            news.collect {
//                newsUpdate(it)
//            }
//        }
//    }
//
//    fun updateCompanies() {
//        scope.launch {
//            updateCompaniesUseCase.invoke()
//        }
//    }
//
//
//
//    fun onDestroy() {
//        scope.onDestroy()
//    }
}
