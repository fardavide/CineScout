
plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    jcenter()
}

dependencies {
    val easyGradle = "1.5-beta-10" // Released: Jun 27, 2020
    val protonGradle =  "0.1.7"    // Released: Jun 22, 2020

    implementation("studio.forface.easygradle:dsl-android:$easyGradle")
    implementation("me.proton.core:util-gradle:$protonGradle")
}
