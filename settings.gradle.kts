rootProject.name = "CineScout"


val (projects, modules) = rootDir.projectsAndModules()

println("Projects: ${projects.joinToString()}")
println("Modules: ${modules.joinToString()}")

for (p in projects) {
    includeBuild(p)
    project(p).name = p.replace(":", "-")
}
for (m in modules) {
    include(m)
    project(m).name = m.replace(":", "-")
}

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
        "src"
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
                modules += newName
                it.find("$newName:")
            }
            else -> it.find("$newName:")
        }
    }

    find()

    return projects to modules
}
