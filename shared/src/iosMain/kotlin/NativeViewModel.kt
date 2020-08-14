package co.touchlab.kampkit

import co.touchlab.stately.ensureNeverFrozen
import domain.data.CompanyData
import domain.use_cases.GetCompaniesForFavouritesUseCase
import domain.use_cases.GetCompanyByTickerUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent


class NativeViewModel(
    private val viewUpdate: (List<CompanyData>) -> Unit,
    private val errorUpdate: (String) -> Unit
) : KoinComponent {

    private val scope = MainScope(Dispatchers.Main)
    private var getCompanyByTickerUseCase: GetCompanyByTickerUseCase
    private var getCompaniesForFavouritesUseCase: GetCompaniesForFavouritesUseCase

    init {
        ensureNeverFrozen()
        getCompanyByTickerUseCase = GetCompanyByTickerUseCase()
        getCompaniesForFavouritesUseCase = GetCompaniesForFavouritesUseCase()
    }

    fun getCompanyByTicker(ticker:String) {
        scope.launch {
            val companyData = getCompanyByTickerUseCase.invoke(ticker)
            viewUpdate(listOf(companyData))
        }
    }

    fun getCompaniesForExplore() {
        scope.launch {
            val companyData = getCompaniesForFavouritesUseCase.invoke()
            viewUpdate(companyData)
        }
    }

    fun onDestroy() {
        scope.onDestroy()
    }
}
