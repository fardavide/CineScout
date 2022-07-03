plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    network {
        this()
        tmdb()
        trakt()
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
