package koin

import kotlinx.coroutines.Dispatchers
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import remote.FinanceRemote
import remote.RemoteFinanceInterface
import sql.DatabaseHelper

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(
        platformModule,
        coreModule
    )
}

private val coreModule = module {
    single {
        DatabaseHelper(
            get(),
            Dispatchers.Default
        )
    }
    single<RemoteFinanceInterface> {
        FinanceRemote()
    }
}

expect val platformModule: Module
