plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    movies {
        domain()
    }
    stats {
        domain()
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
