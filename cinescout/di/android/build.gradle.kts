plugins {
    id("cinescout.android")
}

moduleDependencies {
    design()
    details.presentation()
    di.kotlin()
    home.presentation()
    lists.presentation()
    suggestions.presentation()
}

dependencies {
    implementation(libs.bundles.base)

    testImplementation(libs.bundles.test.kotlin)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit4)
}
