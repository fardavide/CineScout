plugins {
    id("com.android.application")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

val version = studio.forface.easygradle.dsl.Version(0, 1)

android {
    compileSdkVersion(30)

    defaultConfig {
        targetSdkVersion(30)
        minSdkVersion(23)
        versionName = version.versionName
        versionCode = version.versionCode
    }

    sourceSets {
        getByName("main").java.srcDirs("src/androidMain/kotlin")
        getByName("test").java.srcDirs("src/jvmTest/kotlin")
        getByName("androidTest").java.srcDirs("src/androidTest/kotlin")
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
    }
}

kotlin {

    android()

    @Suppress("UNUSED_VARIABLE") // source sets
    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(

                    // Modules
                    entities(),
                    domain(),
                    client(),

                    // Kotlin
                    kotlin("stdlib-jdk8"),
                    coroutines("android"),

                    // UI
                    Android.appCompat(),

                    // Koin
                    koin("android")
                )
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(
                    *commonTestDependencies(),
                    mockk()
                )
            }
        }

        val androidTest by getting {
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
