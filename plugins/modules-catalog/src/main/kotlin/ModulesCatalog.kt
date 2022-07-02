
import Module.Companion.CommonMainSourceSetName
import org.gradle.api.Project

@Suppress("unused")
open class ModulesCatalog internal constructor(project: Project) : Module(project, path = RootPath) {

    init {
        @Suppress("LeakingThis")
        catalog = this
    }

    companion object {

        internal lateinit var catalog: ModulesCatalog
    }
}

/**
 * Paths examples:
 *  * module 'movies': cinescout:cinescout-movies
 *  * module 'movies data': cinescout:movies:cinescout-movies-data
 *  * module 'movies data remote': cinescout:movies:data:cinescout-movies-data-remote
 *  * module 'movies data remote tmdb': cinescout:movies:data:remote:cinescout-movies-data-remote-tmdb
 */
open class Module(
    private val project: Project,
    private val path: String
) {

    internal val normalizedPath: String
        get() = path
            .replace(ColumnPlaceholder, "")
            .replace(DashPlaceholder, "")
            .run {
                val splitByColumn = split(":")
                splitByColumn
                    .filterIndexed { index, _ -> index != splitByColumn.lastIndex - 1 }
                    .joinToString(separator = ":")
                    .removeSurrounding(prefix = "", suffix = "-")
            }

    @Suppress("MemberNameEqualsClassName")
    fun module(name: String) = Module(
        project = project,
        path = path
            .replace(ColumnPlaceholder, "$name:$ColumnPlaceholder")
            .replace(DashPlaceholder, "$name-$DashPlaceholder")
    )

    companion object {

        private const val Placeholder = "*"
        internal const val ColumnPlaceholder = "$Placeholder:"
        internal const val DashPlaceholder = "$Placeholder-"
        const val RootPath = ":cinescout:${ColumnPlaceholder}cinescout-$DashPlaceholder"
        internal const val CommonMainSourceSetName = "commonMain"
    }
}

internal val Any.module: Module get() {
    val modules = this::class.qualifiedName!!
        .split(".")
        .filterNot { it == "ModulesCatalog" }
        .map { it.toLowerCase() }
    var module: Module = ModulesCatalog.catalog
    for (moduleName in modules) {
        module = module.module(moduleName)
    }
    return module
}

internal val Any.sourceSetName get() =
    this::class.java
        .fields
        .find { it.name == "sourceSet" }
        ?.get(this) as? String
        ?: CommonMainSourceSetName

operator fun <T : Any> T.invoke(block: T.() -> Unit) = block()
