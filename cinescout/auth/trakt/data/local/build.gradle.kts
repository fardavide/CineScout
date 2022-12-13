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

kotlin {
    jvm()
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    commonMainImplementation(libs.sqlDelight.coroutines)

    kspJvmOnly(libs.koin.ksp)

    commonTestImplementation(libs.bundles.test.kotlin)
}
