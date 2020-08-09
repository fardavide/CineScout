plugins {
    val kotlinVersion = "1.4.0-rc"

    id("studio.forface.cinescout.gradle")
    kotlin("multiplatform") version kotlinVersion apply false
    kotlin("plugin.serialization") version kotlinVersion apply false
}

buildscript {
    repositories.google()
}

//setupKotlin(
//    "-XXLanguage:+NewInference",
//    "-Xuse-experimental=kotlin.Experimental",
//    "-XXLanguage:+InlineClasses",
//    "-Xopt-in=kotlin.ExperimentalUnsignedTypes"
//)
//setupDetekt { "tokenAutoComplete" !in it.name }
