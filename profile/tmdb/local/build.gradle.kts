import studio.forface.easygradle.dsl.coroutines
import studio.forface.easygradle.dsl.mockK
import studio.forface.easygradle.dsl.serialization
import studio.forface.easygradle.dsl.sqlDelight

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
                    database(),
                    profile(),
                    tmdbProfile(),

                    // Kotlin
                    serialization("json"),
                    coroutines("core"),

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
