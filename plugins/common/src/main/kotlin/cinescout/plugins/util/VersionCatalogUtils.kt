package cinescout.plugins.util

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider

/**
 * Returns the [VersionCatalog] named "libs" from the root project.
 */
val Project.libsCatalog: VersionCatalog
    get() = rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")

val VersionCatalog.composeCompilerVersion: String
    get() = findVersion("composeCompiler").get().toString()

val VersionCatalog.slackComposeLintLibrary: Provider<MinimalExternalModuleDependency>
    get() = findLibrary("slack-compose-lint").get()
