import studio.forface.easygradle.dsl.coroutines
import studio.forface.easygradle.dsl.kermit
import studio.forface.easygradle.dsl.mockK

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

                    // Auth
                    tmdbAuth(),
                    traktAuth(),

                    // Movies
                    tmdbRemoteMovies(),

                    // Profile
                    localTmdbProfile(),
                    remoteTmdbProfile(),
                    localTraktProfile(),
                    remoteTraktProfile(),

                    // Stats
                    localStats(),
                    tmdbRemoteStats(),
                    traktRemoteStats(),

                    // Kotlin
                    kotlin("stdlib-common"),
                    coroutines("core"),

                    // Koin
                    koin("core-ext"),

                    // Other
                    kermit()
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
