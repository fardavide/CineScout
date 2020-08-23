plugins {
    id("com.android.application")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

repositories {
    maven("https://dl.bintray.com/kotlin/kotlin-eap/")
}

configurations.all {
    resolutionStrategy.force("org.jetbrains.kotlin:kotlin-reflect:$KOTLIN_VERSION")
}

//dependencies {
//    implementation(
//
//        // Modules
//        entities(),
//        domain(),
//        client(),
//
//        // Kotlin
//        kotlin("stdlib-jdk8"),
//        coroutines("android"),
//
//        // Android
//        Android.activity(),
//        Android.appCompat(),
//        Android.ktx(),
//
//        // Compose
//        Android.compose("runtime"),
//        Android.compose("ui"),
//        Android.ui("tooling"),
//
//        // Koin
//        koin("android")
//    )
//}

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

                    // Android
                    Android.activity(),
                    Android.appCompat(),
                    Android.ktx(),

                    // Compose
                    Android.compose("runtime"),
                    Android.compose("ui"),
                    Android.ui("tooling"),

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

        val androidMain by getting

        val androidTest by getting {
            dependencies {
                implementation(
                    *jvmTestDependencies(),

                    // Compose
                    Android.ui("test")
                )
            }
        }
    }
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerVersion = "1.4.0-dev-withExperimentalGoogleExtensions-20200720"
        kotlinCompilerExtensionVersion = COMPOSE_VERSION
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

// Configuration accessors
fun org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler.implementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) implementation(dep)
}

fun DependencyHandler.implementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) add("implementation", dep)
}
