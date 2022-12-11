plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    auth {
        tmdb {
            domain()
        }
    }
    utils {
        kotlin()
    }
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    ksp(libs.koin.ksp)
    commonTestImplementation(libs.bundles.test.kotlin)
}

kotlin {
    jvm()
}
