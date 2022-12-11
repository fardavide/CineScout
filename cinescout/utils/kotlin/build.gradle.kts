plugins {
    id("cinescout.kotlin")
}

kotlin {
    jvm()
}

moduleDependencies {
    test.kotlin()
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    ksp(libs.koin.ksp)
    commonTestImplementation(libs.bundles.test.kotlin)
}
