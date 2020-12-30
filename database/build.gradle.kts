plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

sqldelight {
    database("Database") {
        packageName = "database"
    }
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
            kotlin.srcDir("$buildDir/generated/sqldelight/code")

            dependencies {
                implementation(

                    // Modules
                    utils(),
                    entities(),
                    domain(),

                    // Kotlin
                    kotlin("stdlib-common"),
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

        val jvmMain by getting {
            dependencies {
                implementation(
                    sqlDelightDriver("sqlite")
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
                    sqlDelightDriver("android")
                )
            }
        }
    }
}

// Configuration accessors
fun org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler.implementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) implementation(dep)
}
