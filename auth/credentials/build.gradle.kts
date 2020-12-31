import studio.forface.easygradle.dsl.coroutines
import studio.forface.easygradle.dsl.mockK
import studio.forface.easygradle.dsl.serialization
import studio.forface.easygradle.dsl.sqlDelight

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
}

// Issue: https://youtrack.jetbrains.com/issue/KT-43944
android {
    configurations {
        create("testApi")
        create("testDebugApi")
        create("testReleaseApi")
    }
}

kotlin {

    jvm()
    android()

    @Suppress("UNUSED_VARIABLE") // source sets
    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(

                    // Modules
                    utils(),
                    entities(),
                    domain(),
                    database(),

                    // Kotlin
                    kotlin("stdlib-common"),
                    coroutines("core"),
                    serialization("json"),

                    // Koin
                    koin("core-ext"),

                    // SqlDelight
                    sqlDelight("coroutines-extensions")
                )
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(
                    *commonTestDependencies(),
                    mockK()
                )
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(
                    *jvmTestDependencies()
                )
            }
        }
    }
}

// Configuration accessors
fun org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler.implementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) implementation(dep)
}
