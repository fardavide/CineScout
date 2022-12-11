plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    common()
    tvShows.domain()
    store()
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
