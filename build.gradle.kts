plugins {
    val kotlinVersion = "1.4.0"
    val sqlDelightVersion = "1.4.1"

    id("cinescout")
    kotlin("multiplatform") version kotlinVersion apply false
    kotlin("plugin.serialization") version kotlinVersion apply false
    id("com.squareup.sqldelight") version sqlDelightVersion apply false
}

buildscript {
    val agpVersion = "4.2.0-alpha07"

    repositories.google()
    dependencies {
        classpath("com.android.tools.build:gradle:$agpVersion")
    }
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
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-Xallow-jvm-ir-dependencies",
                    "-Xskip-prerelease-check",
                    "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
                )
            }
        }
    }
}
