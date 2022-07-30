plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    account {
        tmdb.domain()
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
