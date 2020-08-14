plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(

        // Modules
        entities(),
        domain(),
        client(),

        // Kotlin
        kotlin("stdlib-jdk8"),
        coroutines("core"),

        // Koin
        koin("core-ext")
    )

    testImplementation(
        *jvmTestDependencies()
    )
}

// Configuration accessors
fun DependencyHandler.implementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) add("implementation", dep)
}
fun DependencyHandler.testImplementation(vararg dependencyNotations: Any) {
    for (dep in dependencyNotations) add("testImplementation", dep)
}
