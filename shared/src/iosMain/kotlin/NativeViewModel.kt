package co.touchlab.kampkit

import co.touchlab.stately.ensureNeverFrozen
import domain.data.CompanyData
import domain.use_cases.GetCompanyByTickerUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent


class NativeViewModel(
    private val viewUpdate: (CompanyData) -> Unit,
    private val errorUpdate: (String) -> Unit
) : KoinComponent {

    private val scope = MainScope(Dispatchers.Main)
    private var getCompanyByTickerUseCase: GetCompanyByTickerUseCase

    init {
        ensureNeverFrozen()
        getCompanyByTickerUseCase = GetCompanyByTickerUseCase()
    }

    private fun getCompanyByTicker(ticker:String) {
        scope.launch {
            getCompanyByTickerUseCase.getCompany(ticker)
        }
    }

    fun onDestroy() {
        scope.onDestroy()
    }
}
