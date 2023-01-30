plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlin)
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("plugin") {
            id = "cinescout.modulesCatalog"
            displayName = "CineScout Modules"
            description = "Add DSL for reference Project modules"
            implementationClass = "CineScoutModulesCatalogPlugin"
        }
    }
}

dependencies {
    implementation(project(":kotlin"))
    implementation(libs.gradle.kotlin)
    testImplementation(libs.bundles.test.kotlin)
}
