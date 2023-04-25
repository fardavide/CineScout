package cinescout.plugins.android

import cinescout.plugins.common.AndroidDefaults
import cinescout.plugins.common.KotlinDefaults
import cinescout.plugins.util.create
import cinescout.plugins.util.withType
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import javax.inject.Inject

open class AndroidOptInsExtension @Inject constructor(private val project: Project) {

    fun androidTestExperimentalTestApi() {
        androidTestOptIn(AndroidDefaults.ExperimentalTestApi)
    }

    fun experimentalCoroutinesApi() {
        optIn(KotlinDefaults.ExperimentalCoroutinesApi)
    }

    fun experimentalPermissionsApi() {
        optIn(AndroidDefaults.ExperimentalPermissionsApi)
    }

    fun experimentalStdlibApi() {
        optIn(KotlinDefaults.ExperimentalStdlibApi)
    }

    fun experimentalTestApi() {
        optIn(AndroidDefaults.ExperimentalTestApi)
    }

    fun flowPreview() {
        optIn(KotlinDefaults.FlowPreview)
    }

    private fun optIn(annotationName: String) {
        project.tasks.withType<KotlinCompile> { task ->
            task.kotlinOptions {
                freeCompilerArgs = freeCompilerArgs + "-opt-in=$annotationName"
            }
        }
    }

    private fun androidTestOptIn(annotationName: String) {
        project.tasks.withType<KotlinCompile> { task ->
            if (task.name.contains("AndroidTest")) {
                task.kotlinOptions {
                    freeCompilerArgs = freeCompilerArgs + "-opt-in=$annotationName"
                }
            }
        }
    }

    companion object {

        fun setup(project: Project): AndroidOptInsExtension = project.extensions.create("optIns")
    }
}
