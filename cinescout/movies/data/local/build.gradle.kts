plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    database()
    movies {
        data()
        domain()
    }
    utils {
        kotlin()
    }
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    commonMainImplementation(libs.sqlDelight.coroutines)

    commonTestImplementation(libs.bundles.test.kotlin)
}

kotlin {
    jvm()
}
