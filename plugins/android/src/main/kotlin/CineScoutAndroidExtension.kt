
import com.android.build.gradle.TestedExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import javax.inject.Inject

open class CineScoutAndroidExtension @Inject constructor(private val project: Project) {

    @Suppress("unused")
    fun androidApp(
        id: String,
        versionCode: Int,
        versionName: String
    ) {
        project.extensions.configure<TestedExtension> {
            defaultConfig {
                this.applicationId = id
                this.versionCode = versionCode
                this.versionName = versionName
            }
        }
    }

    @Suppress("UnstableApiUsage", "unused")
    fun useCompose() {
        project.extensions.configure<TestedExtension> {
            val composeCompilerVersion = project.libsCatalog.findVersion("composeCompiler").get().toString()
            buildFeatures.compose = true
            composeOptions.kotlinCompilerExtensionVersion = composeCompilerVersion
        }
    }

    companion object {

        fun setup(project: Project): CineScoutAndroidExtension =
            project.extensions.create("cinescoutAndroid")
    }
}
