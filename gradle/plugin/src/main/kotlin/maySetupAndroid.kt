import com.android.build.gradle.TestedExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import studio.forface.easygradle.dsl.Version

@Suppress("UnstableApiUsage")
internal fun Project.maySetupAndroid() {

    val isAndroidApplication = plugins.hasPlugin("com.android.application")
    val isAndroidLibrary = plugins.hasPlugin("com.android.library")

    if (isAndroidApplication || isAndroidLibrary) {
        extensions.configure<TestedExtension> {

            compileSdkVersion(30)

            defaultConfig {
                targetSdkVersion(30)
                minSdkVersion(23)

                if (isAndroidApplication) {
                    val appVersion = version as Version
                    versionName = appVersion.versionName
                    versionCode = appVersion.versionCode
                }
            }

            buildFeatures.compose = true

            composeOptions {
                kotlinCompilerVersion = KOTLIN_VERSION
                kotlinCompilerExtensionVersion = COMPOSE_VERSION
            }

            sourceSets {
                getByName("main").java.srcDirs("src/main/kotlin", "src/androidMain/kotlin")
                getByName("test").java.srcDirs("src/test/kotlin")
                getByName("androidTest").java.srcDirs("src/androidTest/kotlin")
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }

            packagingOptions {
                exclude("META-INF/DEPENDENCIES")
            }
        }
    }
}
