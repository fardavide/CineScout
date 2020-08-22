plugins {
    val kotlinVersion = "1.4.0"
    val sqlDelightVersion = "1.4.0"

    id("studio.forface.cinescout.gradle")
    kotlin("multiplatform") version kotlinVersion apply false
    kotlin("plugin.serialization") version kotlinVersion apply false
    id("com.squareup.sqldelight") version sqlDelightVersion apply false
}

buildscript {
    repositories.google()
}

subprojects {

    afterEvaluate {
        extensions.findByType<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension>()?.apply {
            sourceSets {
                all {
                    languageSettings.enableLanguageFeature("InlineClasses")
                    languageSettings.useExperimentalAnnotation("kotlin.time.ExperimentalTime")
                    languageSettings.useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
                    languageSettings.useExperimentalAnnotation("kotlinx.coroutines.FlowPreview")
                    languageSettings.useExperimentalAnnotation("kotlinx.coroutines.ObsoleteCoroutinesApi")
                    languageSettings.useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
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
