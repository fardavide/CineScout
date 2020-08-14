import org.gradle.api.Project

// Client
fun Project.client() = module("client")

// Domain layer
fun Project.entities() = module("entities")
fun Project.domain() = module("domain")

// Data layer
fun Project.network() = module("network")
fun Project.database() = module("database")

// Movies
fun Project.movies() = module("movies")
fun Project.remoteMovies() = module(movies(),"remote")
fun Project.tmdbRemoteMovies() = module(movies(), remoteMovies(),"tmdb")

// Stat
fun Project.stats() = module("stats")
fun Project.localStats() = module(stats(), "local")


private fun Project.module(name: String): Project =
    project(":$name")

private fun Project.module(parent: Project, name: String): Project =
    project(":${parent.name}:$name")

private fun Project.module(parent1: Project, parent2: Project, name: String): Project =
    project(":${parent1.name}:${parent2.name}:$name")
