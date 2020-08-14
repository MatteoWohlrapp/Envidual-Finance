package co.touchlab.kampkit

import co.touchlab.stately.ensureNeverFrozen
import domain.data.CompanyData
import domain.use_cases.GetCompaniesForExploreUseCase
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
    private var getCompaniesForExploreUseCase: GetCompaniesForExploreUseCase

    init {
        ensureNeverFrozen()
        getCompanyByTickerUseCase = GetCompanyByTickerUseCase()
        getCompaniesForExploreUseCase = GetCompaniesForExploreUseCase()
    }

    fun getCompanyByTicker(ticker:String) {
        scope.launch {
            val companyData = getCompanyByTickerUseCase.invoke(ticker)
            viewUpdate(listOf(companyData))
        }
    }

    fun getCompaniesForExplore() {
        scope.launch {
            val companyData = getCompaniesForExploreUseCase.invoke()
            viewUpdate(companyData)
        }
    }

    fun onDestroy() {
        scope.onDestroy()
    }
}
