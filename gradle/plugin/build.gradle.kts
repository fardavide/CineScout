plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.4.21"
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
    val easyGradle = "3.0.5" // Dec 31, 2020
    val agpVersion = "4.2.0-alpha11"

    implementation(gradleApi())
    implementation("studio.forface.easygradle:dsl-android:$easyGradle")
    compileOnly("com.android.tools.build:gradle:$agpVersion")
}
