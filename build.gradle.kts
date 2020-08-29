// Top-level build file where you can add configuration options common to all sub-projects/modules.
import com.example.buildsrc.Deps

buildscript {
    val kotlin_version by extra("1.4.0")
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath(com.example.buildsrc.Deps.android_gradle_plugin)
        classpath(com.example.buildsrc.Deps.SqlDelight.gradle)
        classpath(com.example.buildsrc.Deps.cocoapodsext)
        classpath("org.jetbrains.kotlin:kotlin-serialization:${com.example.buildsrc.Versions.kotlin}")

        classpath(kotlin("gradle-plugin", com.example.buildsrc.Versions.kotlin))
        "classpath"("com.android.tools.build:gradle:4.0.1")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build gradle files
        val nav_version = "2.3.0"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}

plugins {
//    id("org.jlleitschuh.gradle.ktlint") version com.example.buildsrc.Versions.ktlint_gradle_plugin
}

allprojects {
    repositories {
        mavenCentral()
        google()
        jcenter()
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "https://dl.bintray.com/ekito/koin")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

subprojects {
//    apply(plugin = "org.jlleitschuh.gradle.ktlint")
//
//    ktlint {
//        version.set("0.37.2")
//        enableExperimentalRules.set(true)
//        verbose.set(true)
//        filter {
//            exclude { it.file.path.contains("build/") }
//        }
//    }
//
//    afterEvaluate {
//        tasks.named("check").configure {
//            dependsOn(tasks.getByName("ktlintCheck"))
//        }
//    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
