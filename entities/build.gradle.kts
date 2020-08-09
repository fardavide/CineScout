import studio.forface.easygradle.dsl.version

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

                    // Kotlin
                    kotlin("stdlib-common"),
                    serialization("runtime"),

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
                    kotlin("test-common"),
                    kotlin("test-annotations-common"),
                    assert4k()
                )
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(
                    kotlin("test-junit"),
                    kotlinx("coroutines-test") version COROUTINES_TEST_VERSION
                )
            }
        }
    }
}

// Configuration accessors
fun org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler.implementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) implementation(dep)
}
