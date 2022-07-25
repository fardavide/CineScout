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
    dependencies {
        classpath(libs.gradle.android)
        classpath(libs.gradle.crashlytics)
        classpath(libs.gradle.gms)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
