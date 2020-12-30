plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.4.0"
    id("java-gradle-plugin")
}

group = "studio.forface.cinescout"
version = "1.0"

gradlePlugin {
    plugins {
        create("publishPlugin") {
            id = "publish"
            implementationClass = "PublishPlugin"
        }
    }
}

repositories {
    google()
    jcenter()
}

dependencies {
    val easyGradle = "3.0" // Dec 30, 2020

    implementation(gradleApi())
    implementation("studio.forface.easygradle:dsl:$easyGradle")
}
