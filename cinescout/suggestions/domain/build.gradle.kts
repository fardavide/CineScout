plugins {
    id("cinescout.kotlin")
    alias(libs.plugins.kotlin.serialization)
}

moduleDependencies {
    auth {
        tmdb.domain()
        trakt.domain()
    }
    common()
    movies.domain()
    store()
    test.kotlin()
    tvShows.domain()
    utils.kotlin()
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    commonMainImplementation(libs.kotlin.serialization.json)

    ksp(libs.koin.ksp)

    commonTestImplementation(libs.bundles.test.kotlin)
}

kotlin {
    jvm()
}
