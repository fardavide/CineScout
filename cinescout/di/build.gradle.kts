plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    movies {
        data {
            this()
            local()
            remote {
                tmdb()
                trakt()
            }
        }
        domain()
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
    commonTestImplementation(libs.koin.test)
    commonTestImplementation(libs.koin.test.junit4)
}

kotlin {
    jvm()
}
