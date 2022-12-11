plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    database()
    account {
        domain()
        trakt {
            data()
            domain()
        }
    }
    utils {
        kotlin()
    }
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    commonMainImplementation(libs.sqlDelight.coroutines)

    ksp(libs.koin.ksp)

    commonTestImplementation(libs.bundles.test.kotlin)
    commonTestImplementation(libs.sqlDelight.sqlite)
}

kotlin {
    jvm()
}
