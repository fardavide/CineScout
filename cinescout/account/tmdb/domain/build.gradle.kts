plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    account.domain()
    store()
    utils.kotlin()
}

kotlin {
    jvm()
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    kspJvmOnly(libs.koin.ksp)
    commonTestImplementation(libs.bundles.test.kotlin)
}
