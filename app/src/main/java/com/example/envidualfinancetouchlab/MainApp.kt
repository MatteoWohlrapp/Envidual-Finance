package com.example.envidualfinancetouchlab

import AppInfo
import com.example.envidual.finance.touchlab.BuildConfig
import koin.initKoin

//package co.touchlab.kampkit.android

import android.app.Application
import android.content.Context
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module
import android.content.SharedPreferences
import android.util.Log
import org.koin.core.context.KoinContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.koinApplication
//import co.touchlab.kampkit.AppInfo
//import co.touchlab.kampkit.initKoin
import org.koin.dsl.module
import org.koin.ext.scope

class MainApp : Application() {
    override fun onCreate() {
        initKoin(
            module {
                single<Context> { this@MainApp }
                single<AppInfo> { AndroidAppInfo }
            }
        )

        super.onCreate()
    }
}

object AndroidAppInfo : AppInfo {
    override val appId: String = BuildConfig.APPLICATION_ID
}
