plugins {
    id("cinescout.android")
}

cinescoutAndroid.useCompose()

moduleDependencies {
    common()
    design()
    movies.domain()
    network()
    screenplay.domain()
    store()
    test.compose()
    tvShows.domain()
    utils {
        android()
        compose()
        kotlin()
    }
}

dependencies {

    implementation(libs.bundles.base)
    implementation(libs.bundles.compose)
    implementation(libs.accompanist.flowLayout)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.snapper)

    kspJvmOnly(libs.koin.ksp)
    debugImplementation(libs.compose.uiTooling)

    testImplementation(libs.bundles.test.kotlin)
    androidTestImplementation(libs.bundles.test.android)
}
