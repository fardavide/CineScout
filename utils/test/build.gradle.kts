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

                    // Kotlin
                    kotlin("stdlib-common"),
                    coroutines("core"),

                    // Test
                    *commonTestDependencies() - testUtils()
                )
            }
        }

        val jvmMain by getting {
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

operator fun Array<Any>.minus(other: Any) =
    filterNot { it == other }.toTypedArray()
