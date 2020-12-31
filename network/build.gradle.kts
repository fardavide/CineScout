import studio.forface.easygradle.dsl.ktorClient
import studio.forface.easygradle.dsl.mockK
import studio.forface.easygradle.dsl.serialization

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

                    // Kotlin
                    kotlin("stdlib-common"),
                    serialization("json"),

                    // Koin
                    koin("core-ext"),

                    // Ktor
                    ktorClient("core"),
                    ktorClient("serialization"),
                    ktorClient("logging"),
                    ktorClient("mock")
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

        val jvmMain by getting {
            dependencies {
                implementation(
                    ktorClient("apache"),
                    ktorClient("logging-jvm")
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

        val androidMain by getting {
            dependencies {
                implementation(
                    ktorClient("android")
                )
            }
        }
    }
}

// Configuration accessors
fun org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler.implementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) implementation(dep)
}
