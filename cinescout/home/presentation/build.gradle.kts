plugins {
    id("cinescout.android")
}

cinescoutAndroid.useCompose()

moduleDependencies {
    account {
        domain()
        tmdb {
            domain()
        }
        trakt {
            domain()
        }
    }
    auth {
        tmdb {
            domain()
        }
        trakt {
            domain()
        }
    }
    design()
    test {
        compose()
    }
    utils {
        android()
        kotlin()
    }
}

dependencies {
    implementation(libs.bundles.base)
    implementation(libs.bundles.compose)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.bundles.test.kotlin)
    androidTestImplementation(libs.bundles.test.android)
}
