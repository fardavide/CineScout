plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    common()
    database()
    movies {
        data()
        domain()
    }
    test.kotlin()
    utils.kotlin()
}

kotlin {
    jvm()
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    commonMainImplementation(libs.sqlDelight.coroutines)

    kspJvmOnly(libs.koin.ksp)

    commonTestImplementation(libs.bundles.test.kotlin)
    commonTestImplementation(libs.sqlDelight.sqlite)
}
