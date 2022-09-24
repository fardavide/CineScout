plugins {
    id("cinescout.kotlin")
    id("cinescout.android")
}

moduleDependencies {
    database()
}

dependencies {
    commonMainCompileOnly(libs.bundles.base)
    commonMainCompileOnly(libs.bundles.test.kotlin)

    commonMainCompileOnly(libs.koin.core)
    commonMainCompileOnly(libs.koin.test)
    commonMainCompileOnly(libs.koin.test.junit4)
    commonMainCompileOnly(libs.sqlDelight.sqlite)
}

kotlin {
    jvm()
    android()

    sourceSets.named("jvmMain") {
        dependencies {
            compileOnly(libs.sqlDelight.sqlite)
        }
    }
    sourceSets.named("androidMain") {
        dependencies {
            compileOnly(libs.sqlDelight.android)
        }
    }
}
