plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    account.trakt.domain()
    movies.domain()
    test.kotlin()
    utils.kotlin()
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    ksp(libs.koin.ksp)
    commonTestImplementation(libs.bundles.test.kotlin)
}

kotlin {
    jvm()
}
