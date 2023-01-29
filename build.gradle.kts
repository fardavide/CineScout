plugins {
    id("cinescout.detekt")
    id("cinescout.modulesCatalog")
    alias(libs.plugins.kover)
    alias(libs.plugins.ksp)
    alias(libs.plugins.releasesHub)
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.gradle.android)
        classpath(libs.gradle.crashlytics)
        classpath(libs.gradle.gms)
    }
}

apply(from = "gradle/applyProjectRepositories.gradle.kts")
