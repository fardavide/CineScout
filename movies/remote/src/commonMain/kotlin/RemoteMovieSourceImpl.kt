internal class RemoteMovieSourceImpl(
    private val tmdbSource: TmdbRemoteMovieSource
) : RemoteMovieSouce by tmdbSource
