plugins {
    id("cinescout.kotlin")
    alias(libs.plugins.kotlin.serialization)
}

moduleDependencies {
    common()
    utils.kotlin()
}

kotlin {
    jvm()
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    commonMainImplementation(libs.kotlin.serialization.json)

    kspJvmOnly(libs.koin.ksp)

    commonTestImplementation(libs.bundles.test.kotlin)
}
