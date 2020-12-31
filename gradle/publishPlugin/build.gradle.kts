plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.4.21"
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
    val easyGradle = "3.0.5" // Dec 31, 2020

    implementation(gradleApi())
    implementation("studio.forface.easygradle:dsl:$easyGradle")
}
