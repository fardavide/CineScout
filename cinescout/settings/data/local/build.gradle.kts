plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    database()
    settings.data()
    test.kotlin()
    utils.kotlin()
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    commonMainImplementation(libs.sqlDelight.coroutines)

    commonTestImplementation(libs.bundles.test.kotlin)
    commonTestImplementation(libs.sqlDelight.sqlite)
}

kotlin {
    jvm()
}