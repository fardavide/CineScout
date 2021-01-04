import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import studio.forface.easygradle.dsl.Version
import java.io.File

abstract class BumpVersionIfNeededTask : DefaultTask() {

    @get:InputFile
    abstract val versionFile: RegularFileProperty

    /**
     * Whether this Task actually bumped the version
     */
    private var didBump = false

    override fun getDidWork() = didBump

    @TaskAction
    fun action() {
        val textFile = versionFile.get().asFile
        val buildGradleFile = File(project.projectDir, "build.gradle.kts")

        val oldVersionText = textFile.readText().ifEmpty { "0.0.0" }
        val (oldMajor, oldMinor, oldPatch) = oldVersionText.split(".").let {
            val major = (it.getOrElse(0) { "0" }).trim().toInt()
            val minor = (it.getOrElse(1) { "0" }).trim().toInt()
            val patch = (it.getOrElse(2) { "0" }).trim().toInt()
            Triple(major, minor, patch)
        }
        val oldVersion = Version(oldMajor, oldMinor, oldPatch)
        val currentVersion = project.version as Version

        val shouldBump = oldVersion == currentVersion
        if (shouldBump) {
            val newVersion = currentVersion.copy(patch = currentVersion.patch + 1)

            textFile.writeText(newVersion.versionName)
            buildGradleFile.writeText(
                buildGradleFile.readText().replace(
                    currentVersion.gradleString,
                    newVersion.gradleString
                )
            )
            didBump = shouldBump
        } else {
            textFile.writeText(currentVersion.versionName)
        }
    }

    private val Version.gradleString get() =
        if (patch > 0)
            "version = Version($major, $minor, $patch)"
        else
            "version = Version($major, $minor)"
}
