package koin

import co.example.envidual.finance.touchlab.db.EnvidualFinanceDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            EnvidualFinanceDatabase.Schema,
            get(),
            "EnvidualFinanceDatabase"
        )
    }
}