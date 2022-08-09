import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType
import java.io.File

abstract class CineScoutDetektPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.setupDetekt()
    }
}

private fun Project.setupDetekt() {
    val rulesPath = ":detekt:detekt-rules"
    val assembleRules = "$rulesPath:assemble"

    allprojects {
        apply<DetektPlugin>()
    }

    subprojects {

        tasks.withType<Detekt> {
            dependsOn(assembleRules)

            allRules = false
            basePath = rootDir.path
            config.setFrom("${rootDir.path}/detekt/config.yml")
            reports {
                md {
                    required.set(true)
                    outputLocation.set(File("${rootDir.path}/detekt/reports/$name.md"))
                }
            }
        }
    }
}
