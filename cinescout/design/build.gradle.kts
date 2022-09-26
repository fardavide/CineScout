plugins {
    id("cinescout.android")
}

cinescoutAndroid.useCompose()

moduleDependencies {
    test.kotlin()
    utils {
        compose()
        kotlin()
    }
}

dependencies {
    implementation(libs.bundles.base)
    implementation(libs.bundles.compose)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlin.serialization.json)

    debugImplementation(libs.compose.uiTooling)

    testImplementation(libs.bundles.test.kotlin)

    androidTestImplementation(libs.bundles.test.android)
}
