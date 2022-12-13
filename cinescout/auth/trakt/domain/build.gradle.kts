plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    account.trakt.domain()
    movies.domain()
    test.kotlin()
    utils.kotlin()
}

kotlin {
    jvm()
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    kspJvmOnly(libs.koin.ksp)
    commonTestImplementation(libs.bundles.test.kotlin)
}
