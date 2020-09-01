import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import com.example.buildsrc.Deps
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    kotlin("multiplatform")
    id("co.touchlab.native.cocoapods")
    id("kotlinx-serialization")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(com.example.buildsrc.Versions.min_sdk)
        targetSdkVersion(com.example.buildsrc.Versions.target_sdk)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

//    lintOptions {
//        isWarningsAsErrors = false
//        isAbortOnError = false
//    }
}

kotlin {
    android()
    // Revert to just ios() when gradle plugin can properly resolve it
    val onPhone = System.getenv("SDK_NAME")?.startsWith("iphoneos") ?: false
    if (onPhone) {
        iosArm64("ios")
    } else {
        iosX64("ios")
    }
//    targets.getByName<KotlinNativeTarget>("ios").compilations["main"].kotlinOptions.freeCompilerArgs +=
//        listOf("-Xobjc-generics", "-Xg0")

    version = "1.1"

    sourceSets {
        all {
            languageSettings.apply {
                useExperimentalAnnotation("kotlin.RequiresOptIn")
                useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }
    }

    sourceSets["commonMain"].dependencies {
        implementation(kotlin("stdlib-common", com.example.buildsrc.Versions.kotlin))
        implementation(Deps.SqlDelight.runtime)
        implementation(Deps.Ktor.commonCore)
        implementation(Deps.Ktor.commonJson)
        implementation(Deps.Ktor.commonLogging)
        implementation(Deps.Coroutines.jdk)
        implementation(Deps.stately)
        implementation(Deps.multiplatformSettings)
        implementation(Deps.koinCore)
        implementation(Deps.Ktor.commonSerialization)
        api(Deps.kermit)
    }

    sourceSets["commonTest"].dependencies {
        implementation(Deps.multiplatformSettingsTest)
        implementation(Deps.KotlinTest.common)
        implementation(Deps.KotlinTest.annotations)
        implementation(Deps.koinTest)
        // Karmok is an experimental library which helps with mocking interfaces
        implementation(Deps.karmok)
    }

    sourceSets["androidMain"].dependencies {
        implementation(kotlin("stdlib", com.example.buildsrc.Versions.kotlin))
        implementation(Deps.SqlDelight.driverAndroid)
        implementation(Deps.Ktor.jvmCore)
        implementation(Deps.Ktor.jvmJson)
        implementation(Deps.Ktor.jvmLogging)
        implementation(Deps.Coroutines.jdk)
        implementation(Deps.Ktor.androidSerialization)
        implementation(Deps.Ktor.androidCore)
    }

    sourceSets["androidTest"].dependencies {
        implementation(Deps.KotlinTest.jvm)
        implementation(Deps.KotlinTest.junit)
        implementation(Deps.AndroidXTest.core)
        implementation(Deps.AndroidXTest.junit)
        implementation(Deps.AndroidXTest.runner)
        implementation(Deps.AndroidXTest.rules)
        implementation(Deps.Coroutines.test)
        implementation(Deps.robolectric)
    }

    sourceSets["iosMain"].dependencies {
        implementation(Deps.SqlDelight.driverIos)
        implementation(Deps.Ktor.ios)
//        implementation(Deps.Ktor.iosCore)
//        implementation(Deps.Ktor.iosJson)
//        implementation(Deps.Ktor.iosLogging)
        implementation(Deps.Coroutines.jdk) {
            version {
                strictly(com.example.buildsrc.Versions.coroutines)
            }
        }
//        implementation(Deps.Ktor.iosSerialization)
        implementation(Deps.koinCore)
    }

    cocoapodsext {
        summary = "Common library for envidual finance"
        homepage = "https://github.com/MatteoWohlrapp/Envidual-Finance-Touchlab"
        framework {
            isStatic = false
            export(Deps.kermit)
            transitiveExport = true
        }
    }

}



sqldelight {
    database("EnvidualFinanceDatabase") {
        packageName = "co.example.envidual.finance.touchlab.db"
    }
}