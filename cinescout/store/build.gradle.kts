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

    commonTestImplementation(libs.bundles.test.kotlin)
}
