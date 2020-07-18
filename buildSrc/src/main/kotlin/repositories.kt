import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven

/**
 * Lambda that applies repositories required by the project
 * @author Davide Farella
 */
val repos: RepositoryHandler.() -> Unit get() = {
    google()
    maven("https://kotlin.bintray.com/kotlinx")
    maven("https://kotlin.bintray.com/kotlin-eap")
    maven("https://kotlin.bintray.com/kotlin-dev")
    maven("https://4face.bintray.com/4face")
    jcenter()
}
