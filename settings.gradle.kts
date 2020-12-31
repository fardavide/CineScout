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

    fun <T> List<T>.printList() =
        joinToString(prefix = "\n  + ", separator = "\n  + ")

    fun <K, V> Map<K, V>.printMap(): String =
        joinToString(
            key = { it.toString() },
            value = { if (it is List<*>) it.printList() else if (it is Map<*, *>) it.printMap() else TODO() }
        )

    val split = map { it.split(":").filterNot { name -> name.isBlank() } }
    fun List<List<String>>.test(): String =
        groupBy { it.first() }
            .map { (k, v) ->
                k to v.map { list -> if (list.size == 1) list else list.drop(1) }
                k to v.map { list ->
                    when (list.size) {
                        1 -> list
                        2 -> list[1]
                        3 -> "  + ${list[2]}"
                        else -> TODO("support 4th level nesting")
                    }
                }
            }
            .toMap()
            .printMap()

    return split.test()
}

fun <K, V> Map<K, V>.joinToString(key: (K) -> String, value: (V) -> String) =
    map { (k, v) -> "${key(k)}${value(v)}" }.joinToString(separator = "\n")

fun isIntelliJ() =
    System.getenv("__CFBundleIdentifier") == "com.jetbrains.intellij"
