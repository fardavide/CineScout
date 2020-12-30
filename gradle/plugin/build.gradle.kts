plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.4.0"
    id("java-gradle-plugin")
}

group = "studio.forface.cinescout"
version = "1.0"

gradlePlugin {
    plugins {
        create("gradlePlugin") {
            id = "cinescout"
            implementationClass = "GradlePlugin"
        }
    }
}

repositories {
    google()
    jcenter()
}

dependencies {
    val easyGradle = "3.0" // Dec 30, 2020
    val agpVersion = "4.2.0-alpha11"

    implementation(gradleApi())
    implementation("studio.forface.easygradle:dsl:$easyGradle")
    compileOnly("com.android.tools.build:gradle:$agpVersion")
}
