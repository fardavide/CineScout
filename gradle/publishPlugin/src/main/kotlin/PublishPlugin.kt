import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import java.io.File

abstract class PublishPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {

            val bumpVersionIfNeeded = tasks.register<BumpVersionIfNeededTask>("bumpVersionIfNeeded") {
                val file = File(project.projectDir, "releases.txt")
                if (file.exists().not()) file.createNewFile()
                versionFile.set(File("releases.txt"))
            }

            afterEvaluate {

                tasks.register("publishNewVersion") {
                    dependsOn(bumpVersionIfNeeded)
                    // This is not ideal, but I didn't find a better way to generate an apk with the newer version,
                    //  probably because "assembleDebug" would run after that Gradle is already running with the
                    //  previous declared "version"
                    doLast {
                        exec {
                            workingDir = rootProject.rootDir
                            commandLine("./gradlew", "assembleDebug")
                        }
                    }
                }

            }

        }
    }
}
