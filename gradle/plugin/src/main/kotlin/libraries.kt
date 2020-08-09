import studio.forface.easygradle.dsl.version

fun assert4k() = forface("assert4k") version ASSERT4K_VERSION


fun forface(moduleName: String) = "studio.forface:$moduleName"

fun klock() = "com.soywiz.korlibs.klock:klock" version KLOCK_VERSION
fun koin(moduleName: String? = null) = "org.koin:koin${moduleName.module()}" version KOIN_VERSION
fun kotlinx(moduleName: String) = "org.jetbrains.kotlinx:kotlinx-$moduleName"

fun proton(moduleName: String) = "me.proton.core:$moduleName"

fun serialization(moduleName: String? = null) =
    kotlinx("serialization${moduleName.module()}") version SERIALIZATION_VERSION
