plugins {
    id("cinescout.kotlin")
    id("cinescout.android")
}

moduleDependencies {
    store()
    utils.kotlin()
}

kotlin {
    jvm()
    android()

    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
    }
}

dependencies {
    commonMainImplementation(libs.bundles.base)

    commonMainImplementation(libs.ktor.client.contentNegotiation)
    commonMainImplementation(libs.ktor.client.core)
    commonMainImplementation(libs.ktor.client.logging)
    commonMainImplementation(libs.ktor.client.mock)
    commonMainImplementation(libs.ktor.serializationKotlinxJson)

    kspJvmOnly(libs.koin.ksp)

    commonTestImplementation(libs.bundles.test.kotlin)
}
