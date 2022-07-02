plugins {
    id("cinescout.kotlin")
}

kotlin {
    jvm()
}

dependencies {
    commonMainImplementation(libs.bundles.base)

    commonTestImplementation(libs.bundles.test.kotlin)
}
