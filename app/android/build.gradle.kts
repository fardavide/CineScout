plugins {
    id("cinescout.android")
}

cinescoutAndroid {
    val version = System.getenv()["APP_VERSION"] ?: "1"
    useCompose()
    androidApp(
        id = "studio.forface.cinescout2",
        versionCode = version.toInt(),
        versionName = version
    )
}

moduleDependencies {
    auth {
        tmdb.domain()
        trakt.domain()
    }
    common()
    design()
    details.presentation()
    di {
        android()
        kotlin()
    }
    home.presentation()
    lists.presentation()
    movies.domain()
    screenplay.domain()
    suggestions {
        domain()
        presentation()
    }
    test {
        compose()
        kotlin()
        mock()
    }
    tvShows.domain()
}

dependencies {

    implementation(platform(libs.firebase.bom))

    implementation(libs.bundles.base)
    implementation(libs.bundles.compose)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.ktx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewModel)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.workManager)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.kermit.crashlytics)
    implementation(libs.koin.android)
    implementation(libs.koin.android.workManager)

    kspJvmOnly(libs.koin.ksp)
    debugImplementation(libs.bundles.compose.debug)

    testImplementation(libs.bundles.test.kotlin)
    testImplementation(libs.koin.test)
    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.compose.uiTest)
    androidTestImplementation(libs.koin.test)
}
