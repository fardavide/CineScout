plugins {
    id("cinescout.android")
}

cinescoutAndroid.useCompose()

moduleDependencies {
    account {
        domain()
        tmdb.domain()
        trakt.domain()
    }
    auth {
        tmdb.domain()
        trakt.domain()
    }
    design()
    lists.presentation()
    movies.domain()
    store()
    suggestions {
        domain()
        presentation()
    }
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
    debugImplementation(libs.compose.uiTooling)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.bundles.test.kotlin)
    androidTestImplementation(libs.bundles.test.android)
}
