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
    store()
    utils {
        kotlin()
    }
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    commonMainImplementation(libs.kotlin.serialization.json)
    commonMainImplementation(libs.ktor.client.core)
    commonMainImplementation(libs.ktor.client.mock)

    commonTestImplementation(libs.bundles.test.kotlin)
}

kotlin {
    jvm()
}
