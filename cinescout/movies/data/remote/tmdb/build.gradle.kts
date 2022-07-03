plugins {
    id("cinescout.kotlin")
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
        tmdb()
    }
    utils {
        kotlin()
    }
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    commonMainImplementation(libs.ktor.client.core)

    commonTestImplementation(libs.bundles.test.kotlin)
    commonTestImplementation(libs.ktor.client.mock)
}

kotlin {
    jvm()
}
