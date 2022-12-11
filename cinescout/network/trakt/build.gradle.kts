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

dependencies {
    commonMainImplementation(libs.bundles.base)
    commonMainImplementation(libs.ktor.client.auth)
    commonMainImplementation(libs.ktor.client.core)

    ksp(libs.koin.ksp)

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
