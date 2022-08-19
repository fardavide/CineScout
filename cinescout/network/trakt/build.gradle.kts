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
    commonMainImplementation(libs.ktor.client.core)

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
