plugins {
    id("cinescout.kotlin")
    alias(libs.plugins.kotlin.serialization)
}

moduleDependencies {
    account {
        domain()
        tmdb {
            data()
            domain()
        }
    }
    auth.tmdb.domain()
    network {
        this()
        tmdb()
    }
    utils {
        kotlin()
    }
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    commonMainImplementation(libs.kotlin.serialization.json)
    commonMainImplementation(libs.ktor.client.core)
    commonMainImplementation(libs.ktor.client.mock)

    ksp(libs.koin.ksp)

    commonTestImplementation(libs.bundles.test.kotlin)
}

kotlin {
    jvm()
}
