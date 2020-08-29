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

    lintOptions {
        isWarningsAsErrors = false
        isAbortOnError = false
    }
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
    targets.getByName<KotlinNativeTarget>("ios").compilations["main"].kotlinOptions.freeCompilerArgs +=
        listOf("-Xobjc-generics", "-Xg0")

    version = "1.1"

    sourceSets {
        all {
            languageSettings.apply {
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
        implementation(Deps.Coroutines.common)
        implementation(Deps.stately)
        implementation(Deps.multiplatformSettings)
        implementation(Deps.koinCore)
        implementation(Deps.Ktor.commonSerialization)
        api(Deps.kermit)
        implementation("com.google.code.gson:gson:2.8.0")
    }

    sourceSets["commonTest"].dependencies {
        implementation(Deps.multiplatformSettingsTest)
        implementation(Deps.KotlinTest.common)
        implementation(Deps.KotlinTest.annotations)
        implementation(Deps.koinTest)
        // Karmok is an experimental library which helps with mocking interfaces
        implementation(Deps.karmok)
        implementation("com.google.code.gson:gson:2.8.0")

    }

    sourceSets["androidMain"].dependencies {
        implementation(kotlin("stdlib", com.example.buildsrc.Versions.kotlin))
        implementation(Deps.SqlDelight.driverAndroid)
        implementation(Deps.Ktor.jvmCore)
        implementation(Deps.Ktor.jvmJson)
        implementation(Deps.Ktor.jvmLogging)
        implementation(Deps.Coroutines.jdk)
        implementation(Deps.Coroutines.android)
        implementation(Deps.Ktor.androidSerialization)
        implementation(Deps.Ktor.androidCore)
        implementation("com.google.code.gson:gson:2.8.0")

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
        implementation("com.google.code.gson:gson:2.8.0")

    }

    sourceSets["iosMain"].dependencies {
        implementation(Deps.SqlDelight.driverIos)
        implementation(Deps.Ktor.ios)
        implementation(Deps.Ktor.iosCore)
        implementation(Deps.Ktor.iosJson)
        implementation(Deps.Ktor.iosLogging)
        implementation(Deps.Coroutines.native) {
            version {
                strictly("1.3.5-native-mt")
            }
        }
        implementation(Deps.Ktor.iosSerialization)
        implementation(Deps.koinCore)
        implementation("com.google.code.gson:gson:2.8.0")

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

dependencies {
    implementation("com.google.code.gson:gson:2.8.6")
}