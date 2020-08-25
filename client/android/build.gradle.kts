plugins {
    id("com.android.application")
    kotlin("android")
}

dependencies {
    implementation(

        // Modules
        entities(),
        domain(),
        client(),
        design(),

        // Kotlin
        kotlin("stdlib-jdk8"),
        coroutines("android"),

        // Android
        Android.activity(),
        Android.appCompat(),
        Android.ktx(),

        // Compose
        Android.compose("animation"),
        Android.compose("foundation"),
        Android.compose("foundation-layout"),
        Android.compose("material"),
        Android.compose("material-icons-extended"),
        Android.compose("runtime"),
        Android.compose("ui"),
        Android.ui("tooling"),

        // Koin
        koin("android")
    )

    testImplementation(
        *commonTestDependencies(),
        *jvmTestDependencies(),
        mockk(),

        // Compose
        Android.ui("test")
    )

    androidTestImplementation(
        *commonTestDependencies(),
        *jvmTestDependencies(),
        mockk("android"),

        // Compose
        Android.ui("test")
    )
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
        kotlinCompilerVersion = KOTLIN_VERSION
        kotlinCompilerExtensionVersion = COMPOSE_VERSION
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/test/kotlin")
        getByName("androidTest").java.srcDirs("src/androidTest/kotlin")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
    }
}

// Configuration accessors
fun DependencyHandler.implementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) add("implementation", dep)
}

fun DependencyHandler.testImplementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) add("testImplementation", dep)
}

fun DependencyHandler.androidTestImplementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) add("androidTestImplementation", dep)
}
