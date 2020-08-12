import movies.MovieRepository

internal class MovieRepositoryImpl(
    private val removeSource: RemoteMovieSouce
) : MovieRepository by removeSource
