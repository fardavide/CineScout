plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.4.0-rc"
    id("java-gradle-plugin")
}

group = "studio.forface.cinescout"
version = "1.0"

gradlePlugin {
    plugins {
        create("gradlePlugin") {
            id = "studio.forface.cinescout.gradle"
            implementationClass = "GradlePlugin"
        }
    }
}

repositories {
    google()
    jcenter()
}

dependencies {
    val easyGradle = "2.1" // Aug 08, 2020

    implementation(gradleApi())
    implementation("studio.forface.easygradle:dsl:$easyGradle")
}
