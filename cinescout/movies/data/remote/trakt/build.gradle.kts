plugins {
    id("cinescout.kotlin")
    alias(libs.plugins.kotlin.serialization)
}

moduleDependencies {
    auth.trakt.domain()
    common()
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
    screenplay.domain()
    store()
    utils.kotlin()
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
