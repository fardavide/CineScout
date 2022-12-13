plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    movies.domain()
    store()
    test.kotlin()
    tvShows.domain()
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
