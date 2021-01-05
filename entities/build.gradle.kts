import studio.forface.easygradle.dsl.coroutines
import studio.forface.easygradle.dsl.kermit
import studio.forface.easygradle.dsl.klock
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

                    utils(),

                    // Kotlin
                    kotlin("stdlib-common"),
                    coroutines("core"),
                    serialization("core"),

                    // Log
                    kermit(),

                    // Klock
                    klock(),

                    // Koin
                    koin("core-ext")
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
