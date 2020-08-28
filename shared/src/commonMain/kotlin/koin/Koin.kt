package koin

import co.example.envidual.finance.touchlab.db.EnvidualFinanceDatabase
import domain.use_cases.*
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import remote.RemoteFinance
import remote.RemoteFinanceInterface
import sql.DatabaseHelper


fun initKoin(appModule: Module): KoinApplication {
    val koinApplication = startKoin {
        modules(appModule, platformModule, coreModule)
    }

    // Dummy initialization logic, making use of appModule declarations for demonstration purposes.
    val koin = koinApplication.koin


    return koinApplication
}

private val coreModule = module {

    single {
        DatabaseHelper(
            Dispatchers.Default
        )
    }

    single {
        EnvidualFinanceDatabase(get())
    }

    single<RemoteFinanceInterface> {
        RemoteFinance()
    }

    single {
        GetCompanyByTickerUseCase()
    }

    single {
        GetCompaniesForFavouritesUseCase()
    }
    single {
        GetCompaniesForSearchesUseCase()
    }
    single {
        AddCompanyToFavouritesUseCase()
    }
    single {
        DeleteCompanyFromFavouritesUseCase()
    }
    single {
        DeleteCompanyFromSearchesUseCase()
    }
    single {
        UpdateCompaniesUseCase()
    }

}

expect val platformModule: Module
