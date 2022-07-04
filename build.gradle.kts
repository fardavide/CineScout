plugins {
    id("cinescout.detekt")
    id("cinescout.modulesCatalog")
    alias(libs.plugins.kover)
    alias(libs.plugins.ksp)
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
