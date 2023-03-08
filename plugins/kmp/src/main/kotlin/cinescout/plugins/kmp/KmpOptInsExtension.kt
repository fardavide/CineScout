package cinescout.plugins.kmp

import cinescout.plugins.common.KotlinDefaults
import cinescout.plugins.util.configure
import cinescout.plugins.util.create
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import javax.inject.Inject

open class KmpOptInsExtension @Inject constructor(private val project: Project) {

    fun delicateCoroutinesApi() {
        optIn(KotlinDefaults.DelicateCoroutinesApi)
    }

    fun experimentalCoroutinesApi() {
        optIn(KotlinDefaults.ExperimentalCoroutinesApi)
    }

    fun experimentalStdlibApi() {
        optIn(KotlinDefaults.ExperimentalStdlibApi)
    }

    fun flowPreview() {
        optIn(KotlinDefaults.FlowPreview)
    }

    private fun optIn(annotationName: String) {
        project.extensions.configure<KotlinMultiplatformExtension> { ext ->
            ext.sourceSets.all { sourceSet ->
                sourceSet.languageSettings.optIn(annotationName)
            }
        }
    }

    companion object {

        fun setup(project: Project): KmpOptInsExtension = project.extensions.create("optIns")
    }
}
