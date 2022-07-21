
import com.android.build.gradle.TestedExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import javax.inject.Inject

open class CineScoutAndroidExtension @Inject constructor(private val project: Project) {

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

    @Suppress("UnstableApiUsage")
    fun useCompose() {
        project.extensions.configure<TestedExtension> {
            buildFeatures.compose = true
            composeOptions.kotlinCompilerExtensionVersion = "1.1.0"
        }
    }

    companion object {

        fun setup(project: Project): CineScoutAndroidExtension =
            project.extensions.create("cinescoutAndroid")
    }
}
