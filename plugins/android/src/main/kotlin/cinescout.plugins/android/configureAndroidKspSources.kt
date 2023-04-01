package cinescout.plugins.android

import cinescout.plugins.util.configureIfPresent
import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

/**
 * Configures the Android plugin to add generated KSP sources to the Kotlin source sets.
 */
fun configureAndroidKspSources(target: Project) {
    target.extensions.configureIfPresent<AppExtension> { appExtension ->
        appExtension.applicationVariants.all { variant ->
            target.kotlinExtension.sourceSets.getByName(variant.name).kotlin.srcDir(
                "build/generated/ksp/${variant.name}/kotlin"
            )
        }
    }
    target.extensions.configureIfPresent<LibraryExtension> { libraryExtension ->
        libraryExtension.libraryVariants.all { variant ->
            target.kotlinExtension.sourceSets.getByName(variant.name).kotlin.srcDir(
                "build/generated/ksp/${variant.name}/kotlin"
            )
        }
    }
}
