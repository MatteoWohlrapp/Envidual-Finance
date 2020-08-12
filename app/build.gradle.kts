import com.example.buildsrc.Deps
import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(29)
    buildToolsVersion = "29.0.3"
    defaultConfig {
        applicationId = "com.example.envidual_finance_touchlab"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }
    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

//    lintOptions {
//        isWarningsAsErrors = false
//        isAbortOnError = false
//    }
    viewBinding{
        isEnabled = true
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))
    implementation(project(":shared"))
    implementation(Deps.recyclerView)
    implementation(Deps.material_x)
    implementation(Deps.app_compat_x)
    implementation(Deps.core_ktx)
    implementation(Deps.Ktor.androidCore)
    implementation(Deps.constraintlayout)
    implementation(Deps.SqlDelight.runtimeJdk)
    implementation(Deps.SqlDelight.driverAndroid)
    implementation(Deps.Coroutines.jdk)
    implementation(Deps.Coroutines.android)
    implementation(Deps.multiplatformSettings)
    implementation(Deps.koinCore)
    implementation(Deps.lifecycle_viewmodel)
    implementation(Deps.lifecycle_viewmodel_extensions)
    implementation(Deps.lifecycle_livedata)
    implementation(Deps.lifecycle_extension)
    testImplementation(Deps.junit)
}
