plugins {
    id("cinescout.android")
}

cinescoutAndroid.useCompose()

moduleDependencies {
    common()
    design()
    movies.domain()
    screenplay.domain()
    search.presentation()
    settings.domain()
    store()
    suggestions.domain()
    test {
        compose()
        kotlin()
    }
    tvShows.domain()
    utils {
        android()
        compose()
        kotlin()
    }
}

dependencies {

    implementation(platform(libs.firebase.bom))

    implementation(libs.bundles.base)
    implementation(libs.bundles.compose)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.workManager)
    implementation(libs.firebase.analytics)
    implementation(libs.koin.android.workManager)

    kspJvmOnly(libs.koin.ksp)
    debugImplementation(libs.bundles.compose.debug)

    testImplementation(libs.bundles.test.kotlin)
    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.androidx.workManager.test)
}
