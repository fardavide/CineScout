plugins {
    id("cinescout.kotlin")
    alias(libs.plugins.kotlin.serialization)
}

moduleDependencies {
    auth.tmdb.domain()
    common()
    tvShows {
        data {
            this()
            remote()
        }
        domain()
    }
    network {
        this()
        tmdb()
    }
    screenplay.domain()
    store()
    utils {
        kotlin()
    }
}

kotlin {
    jvm()
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    commonMainImplementation(libs.kotlin.serialization.json)
    commonMainImplementation(libs.ktor.client.core)
    commonMainImplementation(libs.ktor.client.mock)

    kspJvmOnly(libs.koin.ksp)

    commonTestImplementation(libs.bundles.test.kotlin)
}
