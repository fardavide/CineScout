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

    implementation(libs.kotlin.serialization.json)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewModel)
    implementation(libs.androidx.workManager)

    testImplementation(libs.bundles.test.kotlin)
}
