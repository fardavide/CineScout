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

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildFeatures.compose = true

            try {
                composeOptions {
                    kotlinCompilerVersion = KOTLIN_VERSION
                    kotlinCompilerExtensionVersion = COMPOSE_VERSION
                }
            } catch (ignored: NoClassDefFoundError) {
                // This will happen on AGP prior 4.2, we need this in order to support build by IntelliJ, which cannot
                // use 4.2 alphas
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
                exclude("META-INF/AL2.0")
                exclude("META-INF/DEPENDENCIES")
                exclude("META-INF/LGPL2.1")
                exclude("META-INF/local.kotlin_module")
                exclude("META-INF/remote.kotlin_module")
                exclude("META-INF/tmdb.kotlin_module")
                exclude("META-INF/tmdb_debug.kotlin_module")
                exclude("META-INF/trakt.kotlin_module")
                exclude("META-INF/trakt_debug.kotlin_module")
            }
        }
    }
}
