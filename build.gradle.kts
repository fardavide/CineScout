plugins {
    val kotlinVersion = "1.4.0-rc"

    id("studio.forface.cinescout.gradle")
    kotlin("multiplatform") version kotlinVersion apply false
    kotlin("plugin.serialization") version kotlinVersion apply false
}

buildscript {
    repositories.google()
}

subprojects {

    afterEvaluate {
        extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension> {
            sourceSets {
                all {
                    languageSettings.enableLanguageFeature("InlineClasses")
                    languageSettings.useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
                }
            }
        }
        tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
}

//setupKotlin(
//    "-XXLanguage:+NewInference",
//    "-Xopt-in=kotlin.ExperimentalUnsignedTypes"
//)
//setupDetekt { "tokenAutoComplete" !in it.name }
