package koin

import android.content.Context
import co.example.EnvidualFinanceDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import io.ktor.client.*
import io.ktor.client.engine.*
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            EnvidualFinanceDatabase.Schema,
            get<Context>(),
            "EnvidualFinanceDatabase"
        )
    }
}
