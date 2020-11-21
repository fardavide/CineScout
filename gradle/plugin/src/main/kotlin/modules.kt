import org.gradle.api.Project

// Utils
fun Project.utils() = module("utils")
fun Project.testUtils() = module(utils(), "test")

// Client
fun Project.client() = module("client")
fun Project.design() = module("design")

// Domain layer
fun Project.entities() = module("entities")
fun Project.domain() = module("domain")

// Data layer
fun Project.network() = module("network")
fun Project.tmdbNetwork() = module(network(), "tmdb")
fun Project.traktNetwork() = module(network(), "trakt")
fun Project.database() = module("database")

// Auth
fun Project.auth() = module("auth")
fun Project.credentials() = module(auth(), "credentials")
fun Project.tmdbAuth() = module(auth(), "tmdb")

// Movies
fun Project.movies() = module("movies")
fun Project.remoteMovies() = module(movies(),"remote")
fun Project.tmdbRemoteMovies() = module(movies(), remoteMovies(),"tmdb")

// Profile
fun Project.profile() = module("profile")
fun Project.tmdbProfile() = module(profile(), "tmdb")
fun Project.localTmdbProfile() = module(profile(), tmdbProfile(), "local")
fun Project.remoteTmdbProfile() = module(profile(), tmdbProfile(), "remote")

// Stat
fun Project.stats() = module("stats")
fun Project.localStats() = module(stats(), "local")
fun Project.remoteStats() = module(stats(), "remote")


private fun Project.module(name: String): Project =
    project(":$name")

private fun Project.module(parent: Project, name: String): Project =
    project(":${parent.name}:$name")

private fun Project.module(parent1: Project, parent2: Project, name: String): Project =
    project(":${parent1.name}:${parent2.name}:$name")
