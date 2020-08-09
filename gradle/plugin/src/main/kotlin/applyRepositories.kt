import org.gradle.api.Project
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories

internal fun Project.applyRepositories() {
    repositories {
        google()
        maven("https://kotlin.bintray.com/kotlinx")
        // maven("https://kotlin.bintray.com/kotlin-eap")
        // maven("https://kotlin.bintray.com/kotlin-dev")
        maven("https://dl.bintray.com/4face/4face")
        maven("https://dl.bintray.com/ekito/koin")
        mavenCentral()
        jcenter()
    }
}
