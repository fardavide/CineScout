package movies

internal class MovieRepositoryImpl(
    private val removeSource: RemoteMovieSource
) : MovieRepository by removeSource
