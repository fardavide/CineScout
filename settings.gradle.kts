rootProject.name = "CineScout"

val (projects, modules) = rootDir.projectsAndModules()

println("Projects: ${projects.sorted().joinToString()}")
println("${modules.size} modules:\n${modules.prettyPrintModules()}")

for (p in projects) includeBuild(p)
for (m in modules) include(m)

enableFeaturePreview("GRADLE_METADATA")

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://plugins.gradle.org/m2/")
    }
}


fun File.projectsAndModules() : Pair<Set<String>, Set<String>> {
    val blacklist = setOf(
        ".git",
        ".gradle",
        ".idea",
        "buildSrc",
        "config",
        "build",
        "src",
        // Skip Android modules as they might be using an incompatible version of AGP for IntelliJ ( see pre-releases )
        if (isIntelliJ()) "android" else ""
    )

    fun File.childrenDirectories() = listFiles { _, name -> name !in blacklist }!!
        .filter { it.isDirectory }

    fun File.isProject() =
        File(this, "settings.gradle.kts").exists() || File(this, "settings.gradle").exists()

    fun File.isModule() = !isProject() &&
            File(this, "build.gradle.kts").exists() || File(this, "build.gradle").exists()


    val modules = mutableSetOf<String>()
    val projects = mutableSetOf<String>()

    fun File.find(name: String? = null): List<File> = childrenDirectories().flatMap {
        val newName = (name ?: "") + it.name
        when {
            it.isProject() -> {
                projects += newName
                emptyList()
            }
            it.isModule() -> {
                modules += ":$newName"
                it.find("$newName:")
            }
            else -> it.find("$newName:")
        }
    }

    find()

    return projects to modules
}

fun Set<String>.prettyPrintModules(): String {

    val split = sorted().map { it.split(":").filterNot { name -> name.isBlank() } }

    val folders = split.map { moduleHierarchy ->
        var last: Folder? = null
        moduleHierarchy.reversed().mapIndexedNotNull { index, name ->
            last = Folder(
                name,
                level = moduleHierarchy.size - index,
                isModule = last == null,
                children = last?.let(::listOf) ?: emptyList()
            )
            last.takeIf { index == moduleHierarchy.lastIndex }
        }
    }.flatten()

    val root = Folder("root", 0, false, folders)
        .mergeChildren()

    return root.children.joinToString(separator = "\n") { it.prettyPrint() }
}

data class Folder(
    val name: String,
    val level: Int,
    val isModule: Boolean,
    val children: List<Folder>
) {

    fun mergeChildren(): Folder {
        val merged: List<Folder> = children
            .groupBy { child -> child.name }
            .map { (name, children) ->
                Folder(
                    name,
                    level + 1,
                    isModule = children.any { it.isModule },
                    children = children.flatMap { it.children }
                )
            }
            .map { it.mergeChildren() }
        return Folder(name, level, isModule, merged)
    }

    fun prettyPrint(): String = prettyPrintName() +
        children.toString { "\n${indent()}+ ${it.prettyPrint()}" }

    private fun prettyPrintName() =
        "${if (isModule) "[" else ""}$name${if (isModule) "]" else ""}"

    private fun indent() =
        (1..level).toList().toString { "  " }

    private fun <T> List<T>.toString(transform: (T) -> String) =
        joinToString(separator = "", transform = transform)
}

fun isIntelliJ() =
    System.getenv("__CFBundleIdentifier") == "com.jetbrains.intellij"
