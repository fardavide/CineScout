plugins {
    id("cinescout.kotlin")
    alias(libs.plugins.kotlin.serialization)
}

moduleDependencies {

    movies {
        data {
            this()
            remote()
        }
        domain()
    }
    network {
        this()
        trakt()
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
