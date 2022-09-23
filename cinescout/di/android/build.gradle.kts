plugins {
    id("cinescout.android")
}

moduleDependencies {
    design()
    details.presentation()
    di.kotlin()
    home.presentation()
    lists.presentation()
    search.presentation()
    suggestions.presentation()
}

dependencies {
    implementation(libs.bundles.base)

    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.koin.test)
    androidTestImplementation(libs.koin.test.junit4)

    androidTestImplementation(libs.androidx.workManager)
    androidTestImplementation(libs.firebase.analytics)
}
