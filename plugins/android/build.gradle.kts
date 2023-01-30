
plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlin)
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("plugin") {
            id = "cinescout.android"
            displayName = "CineScout Android"
            description = "Configure Android module"
            implementationClass = "CineScoutAndroidPlugin"
        }
    }
}

dependencies {

    implementation(project(":kotlin"))
    implementation(libs.gradle.android)
    implementation(libs.gradle.kotlin)
}
