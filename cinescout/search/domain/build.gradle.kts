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

dependencies {
    commonMainImplementation(libs.bundles.base)
    ksp(libs.koin.ksp)
    commonTestImplementation(libs.bundles.test.kotlin)
}

kotlin {
    jvm()
}
