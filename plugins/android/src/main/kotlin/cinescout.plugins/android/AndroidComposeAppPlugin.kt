package cinescout.plugins.android

import cinescout.plugins.common.AndroidDefaults
import cinescout.plugins.util.apply
import cinescout.plugins.util.configure
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

@Suppress("unused")
internal class AndroidComposeAppPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target.pluginManager) {
            apply("com.android.application")
            apply<AndroidPlugin>()
            apply<AndroidComposePlugin>()
        }

        target.extensions.configure<ApplicationExtension> { ext ->
            configureAndroidApplicationExtension(ext, target.rootDir)
        }
    }

    @Suppress("UnstableApiUsage")
    private fun configureAndroidApplicationExtension(ext: ApplicationExtension, rootDir: File) {
        ext.defaultConfig {
            applicationId = AndroidDefaults.APPLICATION_ID
            versionCode = VersionCode
            versionName = VersionName
            targetSdk = AndroidDefaults.TARGET_SDK
        }
        ext.signingConfigs {
            create("release") { config ->
                config.storeFile = File("${rootDir.path}/keystore/keystore.jks")
                config.storePassword = System.getenv("KEYSTORE_PASSWORD")
                config.keyAlias = System.getenv("KEYSTORE_KEY_ALIAS")
                config.keyPassword = System.getenv("KEYSTORE_KEY_PASSWORD")
            }
        }

        ext.buildTypes {
            named("release") { config ->
                config.isMinifyEnabled = false
                config.signingConfig = ext.signingConfigs.getByName("release")
                config.manifestPlaceholders["crashlyticsCollectionEnabled"] = true
            }
            named("debug") { config ->
                config.manifestPlaceholders["crashlyticsCollectionEnabled"] = false
            }
        }
    }

    companion object {

        val VersionName = System.getenv()["APP_VERSION"] ?: "1"
        val VersionCode = VersionName.toInt()
    }
}
