plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    utils {
        kotlin()
    }
}

dependencies {
    commonMainImplementation(libs.bundles.base)

    commonMainImplementation(libs.ktor.client.contentNegotiation)
    commonMainImplementation(libs.ktor.client.core)
    commonMainImplementation(libs.ktor.client.logging)
    commonMainImplementation(libs.ktor.client.mock)
    commonMainImplementation(libs.ktor.serializationKotlinxJson)

    ksp(libs.ksp.koin)

    commonTestImplementation(libs.bundles.test.kotlin)
}

kotlin {
    jvm()

    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
    }
}
