plugins {
    id("cinescout.android")
}

cinescoutAndroid.useCompose()

moduleDependencies {
    common()
    design()
    movies.domain()
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
    debugImplementation(libs.compose.uiTooling)

    implementation(libs.androidx.lifecycle.runtime)

    testImplementation(libs.bundles.test.kotlin)
    androidTestImplementation(libs.bundles.test.android)
}
