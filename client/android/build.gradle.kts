import studio.forface.easygradle.dsl.Version
import studio.forface.easygradle.dsl.android.accompanist
import studio.forface.easygradle.dsl.android.activity
import studio.forface.easygradle.dsl.android.androidCompose
import studio.forface.easygradle.dsl.android.androidKtx
import studio.forface.easygradle.dsl.android.androidUi
import studio.forface.easygradle.dsl.android.appcompat
import studio.forface.easygradle.dsl.archivesBaseName
import studio.forface.easygradle.dsl.assert4k
import studio.forface.easygradle.dsl.coroutines
import studio.forface.easygradle.dsl.exclude
import studio.forface.easygradle.dsl.kermit
import studio.forface.easygradle.dsl.mockK
import studio.forface.easygradle.dsl.version

plugins {
    id("com.android.application")
    kotlin("android")
    id("publish")
}

version = Version(0, 1, 4)
archivesBaseName =
    "cinescout_${(version as Version).versionName.replace(".", "_")}"

dependencies {
    implementation(

        // Modules
        utils(),
        entities(),
        domain(),
        client(),
        design(),

        // Kotlin
        coroutines("android"),

        // Log
        kermit(),

        // Android
        activity(),
        appcompat(),
        androidKtx(),

        // Compose
        accompanist("coil"),
        androidCompose("animation"),
        androidCompose("foundation"),
        androidCompose("foundation-layout"),
        androidCompose("material"),
        androidCompose("material-icons-extended"),
        androidCompose("runtime"),
        androidCompose("ui"),
        androidCompose("ui-tooling"),

        // Koin
        koin("android")
    )

    testImplementation(
        *commonTestDependencies(),
        *jvmTestDependencies(),
        mockK(),

        // Compose
        androidUi("test") version "1.0.0-alpha07"
    )

    androidTestImplementation(
        *commonTestDependencies(),
        *jvmTestDependencies(),
        mockK("android"),

        // Compose
        androidUi("test") version "1.0.0-alpha07"
    )
    configurations["androidTestImplementation"].exclude(assert4k())
}

// region Configuration accessors
fun DependencyHandler.implementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) add("implementation", dep)
}

fun DependencyHandler.testImplementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) add("testImplementation", dep)
}

fun DependencyHandler.androidTestImplementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) add("androidTestImplementation", dep)
}
// endregion
