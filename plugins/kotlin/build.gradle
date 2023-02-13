plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlin)
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("plugin") {
            id = "cinescout.kotlin"
            displayName = "CineScout Kotlin"
            description = "Configure Kotlin module"
            implementationClass = "CineScoutKotlinPlugin"
        }
    }
}

dependencies {

    implementation(libs.gradle.kotlin)
}