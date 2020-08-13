package koin

import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import remote.FinanceRemote
import remote.RemoteFinanceInterface
import sql.ExploreDatabaseHelper


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
        ExploreDatabaseHelper(
            get(),
            Dispatchers.Default
        )
    }
    single<RemoteFinanceInterface> {
        FinanceRemote()
    }
}

expect val platformModule: Module
