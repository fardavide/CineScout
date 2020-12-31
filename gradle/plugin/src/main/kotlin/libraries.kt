import org.gradle.api.Project
import studio.forface.easygradle.dsl.`koin version`
import studio.forface.easygradle.dsl.assert4k
import studio.forface.easygradle.dsl.coroutines
import studio.forface.easygradle.dsl.koin
import studio.forface.easygradle.dsl.kotlin
import studio.forface.easygradle.dsl.version

fun koin(moduleName: String? = null, useMultiplatform: Boolean = true) =
    koin(module = moduleName) version if (useMultiplatform) `koin-mp version` else `koin version`


fun Project.commonTestDependencies(): Array<Any> = arrayOf(
    kotlin("test-common"),
    kotlin("test-annotations-common"),
    assert4k(),
    koin("test"),
    testUtils()
)

fun jvmTestDependencies() = arrayOf(
    kotlin("test-junit"),
    coroutines("test")
)
