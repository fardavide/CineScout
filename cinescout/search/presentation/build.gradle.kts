plugins {
    id("cinescout.android")
}

cinescoutAndroid.useCompose()

moduleDependencies {
    common()
    design()
    movies.domain()
    screenplay.domain()
    search.domain()
    store()
    test {
        compose()
        kotlin()
    }
    tvShows.domain()
    utils {
        android()
        kotlin()
    }
}

dependencies {
    implementation(libs.bundles.base)
    implementation(libs.bundles.compose)
    implementation(libs.androidx.lifecycle.runtime)

    kspJvmOnly(libs.koin.ksp)
    debugImplementation(libs.bundles.compose.debug)

    testImplementation(libs.bundles.test.kotlin)
    androidTestImplementation(libs.bundles.test.android)
}
