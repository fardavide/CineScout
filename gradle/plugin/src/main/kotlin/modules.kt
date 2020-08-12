import org.gradle.api.Project

// Domain layer
fun Project.entities() = project(":entities")
fun Project.domain() = project(":domain")

// Data layer
fun Project.network() = project(":network")

// Movies
fun Project.movies() = project(":movies")
fun Project.remoteMovies() = project(":movies:remote")
fun Project.tmdbRemoteMovies() = project(":movies:remote:tmdb")
