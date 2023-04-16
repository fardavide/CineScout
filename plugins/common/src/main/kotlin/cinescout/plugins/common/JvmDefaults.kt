package cinescout.plugins.common

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object JvmDefaults {

    const val WARNINGS_AS_ERRORS = true
    const val JAVA_VERSION = 19
    val Target = JvmTarget.JVM_19
}
