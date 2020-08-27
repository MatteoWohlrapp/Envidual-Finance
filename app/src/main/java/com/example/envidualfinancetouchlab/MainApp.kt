package com.example.envidualfinancetouchlab

import AppInfo
import com.example.envidual.finance.touchlab.BuildConfig
import android.app.Application
import android.content.Context
import koin.initKoin
import org.koin.dsl.module

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(
            module {
                single<Context> { this@MainApp }
            }
        )

    }
}

object AndroidAppInfo : AppInfo {
    override val appId: String = BuildConfig.APPLICATION_ID
}
