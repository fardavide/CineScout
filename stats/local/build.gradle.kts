import studio.forface.easygradle.dsl.coroutines
import studio.forface.easygradle.dsl.klock
import studio.forface.easygradle.dsl.mockK
import studio.forface.easygradle.dsl.sqlDelight

plugins {
    kotlin("multiplatform")
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
                    database(),
                    stats(),

                    // Kotlin
                    kotlin("stdlib-common"),
                    coroutines("core"),

                    // Other
                    klock(),
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
                    *jvmTestDependencies(),
                    sqlDelight("sqlite-driver")
                )
            }
        }
    }
}

// Configuration accessors
fun org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler.implementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) implementation(dep)
}
