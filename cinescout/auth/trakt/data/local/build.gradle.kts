plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    auth {
        trakt {
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

    commonTestImplementation(libs.bundles.test.kotlin)
}

kotlin {
    jvm()
}
