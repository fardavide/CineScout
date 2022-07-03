
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

open class Module(
    private val project: Project,
    private val path: List<String>
) {

    internal val normalizedPath: String
        get() = buildString {
            val partial = mutableListOf<String>()
            for ((index, segment) in path.withIndex()) {
                partial += segment
                if (index > 0) append(ColumnSeparator)
                append(partial.joinToString(separator = DashSeparator))
            }
        }

    @Suppress("MemberNameEqualsClassName")
    fun module(name: String) = Module(
        project = project,
        path = path + name
    )

    companion object {

        internal const val ColumnSeparator = ":"
        internal const val DashSeparator = "-"
        val RootPath = listOf("cinescout")
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
