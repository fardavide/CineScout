plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {

    jvm()

    @Suppress("UNUSED_VARIABLE") // source sets
    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(
                    kotlin("stdlib-common"),
                    serialization("runtime"),
                    koin("core-ext")
                )
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(
                    kotlin("test-common"),
                    kotlin("test-annotations-common"),
                    assert4k()
                )
            }
        }
    }
}

// Configuration accessors
fun org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler.implementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) implementation(dep)
}
