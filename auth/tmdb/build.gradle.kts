import studio.forface.easygradle.dsl.ktorClient
import studio.forface.easygradle.dsl.mockK
import studio.forface.easygradle.dsl.serialization

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {

    jvm()

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
                    mockK(),
                    ktorClient("mock")
                )
            }
        }

    }
}

// Configuration accessors
fun org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler.implementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) implementation(dep)
}
