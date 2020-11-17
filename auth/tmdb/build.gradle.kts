plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
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
                    credentials(),
                    network(),
                    tmdbNetwork(),

                    // Kotlin
                    serialization("json"),

                    // Koin
                    koin("core-ext"),

                    // Ktor
                    ktorClient("core")
                )
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(
                    *commonTestDependencies()
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
                    *jvmTestDependencies(),
                    mockk(),
                    ktorClient("mock")
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
