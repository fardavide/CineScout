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

kotlin {
    jvm()
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    commonMainImplementation(libs.sqlDelight.coroutines)

    kspJvmOnly(libs.koin.ksp)

    commonTestImplementation(libs.bundles.test.kotlin)
}
