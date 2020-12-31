import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import studio.forface.easygradle.dsl.EasyGradlePlugin
import studio.forface.easygradle.dsl.android.EasyGradleAndroidPlugin
import studio.forface.easygradle.dsl.isAndroid

plugins {
    val kotlinVersion = "1.4.21" // Nov 19, 2020
    val sqlDelightVersion = "1.4.4" // Oct 08, 2020
    val easyGradleVersion = "3.0.5" // Dec 31, 2020

    id("cinescout")
    kotlin("multiplatform") version kotlinVersion apply false
    kotlin("plugin.serialization") version kotlinVersion apply false
    id("com.squareup.sqldelight") version sqlDelightVersion apply false
    id("studio.forface.easygradle") version easyGradleVersion
    id("studio.forface.easygradle-android") version easyGradleVersion
}

buildscript {
    fun isIntelliJ() =
        System.getenv("__CFBundleIdentifier") == "com.jetbrains.intellij"

    val agpVersion =
        if (isIntelliJ()) "4.0.1"
        else "7.0.0-alpha03" // Dec 15, 2020
    val koinVersion = "3.0.0-alpha-4" // Oct 01, 2020

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:$agpVersion")
        classpath("org.koin:koin-gradle-plugin:$koinVersion")

        // Work around for java.lang.ClassNotFoundException: org.apache.batik.w3c.dom.ElementTraversal
        classpath("org.apache.xmlgraphics:batik-ext:1.13")
    }
}

subprojects {

    apply(plugin = "koin")

    if (isAndroid)
        apply<EasyGradleAndroidPlugin>()
    else
        apply<EasyGradlePlugin>()

    afterEvaluate {
        extensions.findByType<KotlinMultiplatformExtension>()?.apply {
            sourceSets.all {
                languageSettings.apply {
                    enableLanguageFeature("InlineClasses")
                    useExperimentalAnnotation("kotlin.time.ExperimentalTime")
                    useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
                    useExperimentalAnnotation("kotlinx.coroutines.FlowPreview")
                    useExperimentalAnnotation("kotlinx.coroutines.ObsoleteCoroutinesApi")
                    useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
                }
            }
        }
        tasks.withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-Xallow-jvm-ir-dependencies",
                    "-Xskip-prerelease-check",
                    "-Xopt-in=kotlin.time.ExperimentalTime",
                    "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
                )
            }
        }
    }
}
