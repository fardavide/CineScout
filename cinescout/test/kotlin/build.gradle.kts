plugins {
    id("cinescout.kotlin")
}

kotlin {
    jvm()
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    // commonMainImplementation(libs.bundles.test.kotlin)
}
