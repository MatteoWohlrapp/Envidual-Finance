package koin

import AppInfo
import co.touchlab.kermit.Kermit
import io.ktor.http.ContentType
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinApplication
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import remote.FinanceRemote
import remote.RemoteFinanceInterface
import sql.DatabaseHelper


fun initKoin(appModule: Module): KoinApplication {
    val koinApplication = startKoin {
        loadKoinModules( listOf(appModule, platformModule, coreModule)
        )
    }

    // Dummy initialization logic, making use of appModule declarations for demonstration purposes.
    val koin = koinApplication.koin
    val doOnStartup = koin.get<() -> Unit>() // doOnStartup is a lambda which is implemented in Swift on iOS side
    doOnStartup.invoke()

    val kermit = koin.get<Kermit> { parametersOf(null) }
    val appInfo = koin.get<AppInfo>() // AppInfo is a Kotlin interface with separate Android and iOS implementations
    kermit.v { "App Id ${appInfo.appId}" }

    return koinApplication
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
