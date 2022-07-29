plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    auth {
        trakt {
            domain()
            data()
        }
    }
    database()
    network {
        trakt()
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
