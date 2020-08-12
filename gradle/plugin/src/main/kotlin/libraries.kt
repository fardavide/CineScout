import studio.forface.easygradle.dsl.version

fun assert4k() = forface("assert4k") version ASSERT4K_VERSION

fun forface(moduleName: String) = "studio.forface:$moduleName"

fun klock() = "com.soywiz.korlibs.klock:klock" version KLOCK_VERSION
fun koin(moduleName: String? = null) = "org.koin:koin${moduleName.module()}" version KOIN_VERSION
fun kotlin(moduleName: String) = "org.jetbrains.kotlin:kotlin-$moduleName" version KOTLIN_VERSION
fun kotlinx(moduleName: String) = "org.jetbrains.kotlinx:kotlinx-$moduleName"

fun ktor(moduleName: String) = "io.ktor:ktor-$moduleName" version KTOR_VERSION
fun ktorClient(moduleName: String) = ktor("client-$moduleName")

fun proton(moduleName: String) = "me.proton.core:$moduleName"

fun serialization(moduleName: String? = null) =
    kotlinx("serialization${moduleName.module()}") version SERIALIZATION_VERSION

// Groups
fun commonTestDependencies() = arrayOf(
    kotlin("test-common"),
    kotlin("test-annotations-common"),
    assert4k()
)

fun jvmTestDependencies() = arrayOf(
    kotlin("test-junit"),
    kotlinx("coroutines-test") version COROUTINES_TEST_VERSION
)
