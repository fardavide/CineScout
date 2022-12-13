plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    network()
    store()
    utils {
        kotlin()
    }
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

dependencies {
    commonMainImplementation(libs.bundles.base)
    commonMainImplementation(libs.ktor.client.auth)
    commonMainImplementation(libs.ktor.client.core)

    kspJvmOnly(libs.koin.ksp)

    commonTestImplementation(libs.bundles.test.kotlin)
}
