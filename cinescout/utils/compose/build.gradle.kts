plugins {
    id("cinescout.android")
}

cinescoutAndroid.useCompose()

moduleDependencies {
    test {
        kotlin()
    }
    utils {
        android()
        kotlin()
    }
}

dependencies {
    implementation(libs.bundles.base)
    implementation(libs.bundles.compose)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.workManager)
    implementation(libs.material3.windowSizeClass)

    testImplementation(libs.bundles.test.kotlin)

    androidTestImplementation(libs.bundles.test.android)
}
