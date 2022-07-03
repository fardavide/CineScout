plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    movies {
        data {
            remote()
        }
        domain()
    }
    network {
        tmdb()
    }
    utils {
        kotlin()
    }
}

dependencies {
    commonMainImplementation(libs.bundles.base)

    commonTestImplementation(libs.bundles.test.kotlin)
}

kotlin {
    jvm()
}
