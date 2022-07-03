plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    movies {
        data()
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
