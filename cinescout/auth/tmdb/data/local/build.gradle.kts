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
    network {
        tmdb()
    }
    utils {
        kotlin()
    }
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    commonMainImplementation(libs.sqlDelight.coroutines)

    commonTestImplementation(libs.bundles.test.kotlin)
}

kotlin {
    jvm()
}
