plugins {
    id("cinescout.kotlin")
    alias(libs.plugins.kotlin.serialization)
}

moduleDependencies {
    auth {
        trakt {
            data()
            domain()
        }
    }
    network {
        this()
        trakt()
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

    commonTestImplementation(libs.bundles.test.kotlin)
}

kotlin {
    jvm()
}
