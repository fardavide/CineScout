plugins {
    id("cinescout.kotlin")
}

kotlin {
    jvm()
}

moduleDependencies {
    database()
    test.kotlin()
    utils.kotlin()
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    ksp(libs.koin.ksp)
    commonTestImplementation(libs.bundles.test.kotlin)
}
