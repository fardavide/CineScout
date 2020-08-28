import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

abstract class GradlePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.applyRepositories()
        target.afterEvaluate { maySetupAndroid() }

        target.subprojects {
            apply<GradlePlugin>()
        }
    }
}
