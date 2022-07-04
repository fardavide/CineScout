plugins {
    id("cinescout.kotlin")
    alias(libs.plugins.kotlin.serialization)
}

moduleDependencies {
    utils {
        kotlin()
    }
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    commonMainImplementation(libs.kotlin.serialization.json)

    commonTestImplementation(libs.bundles.test.kotlin)
}

kotlin {
    jvm()
}
