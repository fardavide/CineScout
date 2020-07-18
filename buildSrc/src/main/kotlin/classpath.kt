
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.ScriptHandlerScope
import studio.forface.easygradle.dsl.*
import studio.forface.easygradle.dsl.android.*

/**
 * Lambda that applies dependencies to the classpath
 * @author Davide Farella
 */
val ScriptHandlerScope.classpathDependencies: DependencyHandlerScope.() -> Unit get() = {
    classpath(`android-gradle-plugin`)
    classpath(`kotlin-gradle-plugin` version "1.4-M3")
    classpath(`serialization-gradle-plugin`)
}
