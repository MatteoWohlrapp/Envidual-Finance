package koin

import cache.*
import co.example.envidual.finance.touchlab.db.EnvidualFinanceDatabase
import domain.use_cases.*
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import remote.RemoteFinance
import remote.RemoteFinanceInterface


fun initKoin(appModule: Module): KoinApplication {
    val koinApplication = startKoin {
        modules(appModule, platformModule, coreModule)
    }
    return koinApplication
}

private val coreModule = module {

    single {
        DatabaseHelper(
            Dispatchers.Default,
            get()
        )
    }

    single {
        EnvidualFinanceDatabase(get())
    }

    single {
        HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }
    single<RemoteFinanceInterface> {
        RemoteFinance(get())
    }
    single<CompanyDataCacheInterface> {
        CompanyDataCache(
            Dispatchers.Default,
             get()
        )
    }

    single<CompanyNewsCacheInterface>{
        CompanyNewsCache(
            Dispatchers.Default,
            get()
        )
    }

    single {
        GetCompanyByTickerUseCase(get(), get())
    }

    single {
        GetCompaniesForFavouritesUseCase(get(), get())
    }
    single {
        GetCompaniesForSearchesUseCase(get())
    }
    single {
        AddCompanyToFavouritesUseCase(get())
    }
    single {
        DeleteCompanyFromFavouritesUseCase(get())
    }
    single {
        DeleteCompanyFromSearchesUseCase(get())
    }
    single {
        UpdateCompaniesUseCase(get(), get())
    }
    single {
        GetCompanyNewsByTickerUseCase(get(), get())
    }

}

expect val platformModule: Module
