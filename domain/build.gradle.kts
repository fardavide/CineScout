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

                    // Modules
                    utils(),
                    entities(),

                    // Kotlin
                    kotlin("stdlib-common"),
                    coroutines("core"),
                    serialization("core"),

                    // Koin
                    koin("core-ext"),

                    // Other
                    kermit(),
                    klock()
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

        val jvmTest by getting {
            dependencies {
                implementation(
                    *jvmTestDependencies(),
                    mockK()
                )
            }
        }
    }
}

// Configuration accessors
fun org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler.implementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) implementation(dep)
}
