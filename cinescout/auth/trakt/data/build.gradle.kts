plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    auth {
        trakt {
            domain()
        }
    }
    utils {
        kotlin()
    }
}

kotlin {
    jvm()
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    kspJvmOnly(libs.koin.ksp)
    commonTestImplementation(libs.bundles.test.kotlin)
}
