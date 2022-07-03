plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    movies {
        domain()
        data {
            local()
            remote {
                tmdb()
                trakt()
            }
        }
    }
    network {
        this()
        tmdb()
        trakt()
    }
    suggestions {
        domain()
    }
    utils {
        kotlin()
    }
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    commonMainImplementation(project(":cinescout:cinescout-network:cinescout-network-tmdb"))

    ksp(libs.ksp.koin)

    commonTestImplementation(libs.bundles.test.kotlin)
}

kotlin {
    jvm()
}
