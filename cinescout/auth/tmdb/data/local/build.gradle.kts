plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    auth {
        tmdb {
            data()
        }
    }
    database()
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
