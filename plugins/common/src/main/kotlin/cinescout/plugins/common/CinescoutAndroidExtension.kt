package cinescout.plugins.common

import cinescout.plugins.util.configure
import cinescout.plugins.util.create
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import javax.inject.Inject

open class CinescoutAndroidExtension @Inject constructor(private val project: Project) {

    fun namespace(namespace: String) {
        project.extensions.configure<CommonExtension<*, *, *, *>> { ext ->
            ext.namespace = namespace
        }
    }

    companion object {

        fun setup(project: Project): CinescoutAndroidExtension =
            project.extensions.create("cinescoutAndroid")
    }
}
