plugins {
    id("cinescout.android")
}

cinescoutAndroid.useCompose()

moduleDependencies {
    common()
    design()
    movies.domain()
    store()
    test.compose()
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
