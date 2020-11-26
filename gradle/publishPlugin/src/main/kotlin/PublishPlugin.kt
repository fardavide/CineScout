import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import java.io.File

abstract class PublishPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register<BumpVersionIfNeededTask>("bumpVersionIfNeeded") {
            val file = File(project.projectDir, "releases.txt")
            if (file.exists().not()) file.createNewFile()
            versionFile.set(File("releases.txt"))
        }
    }
}
