plugins {
    id("cinescout.android")
}

moduleDependencies {
    utils {
        kotlin()
    }
}

dependencies {
    implementation(libs.bundles.base)
    implementation(libs.bundles.compose)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.bundles.test.kotlin)
}
