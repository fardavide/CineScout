package movies.remote

import movies.RemoteMovieSource

class RemoteMovieSourceImpl(
    private val tmdbSource: TmdbRemoteMovieSource
) : RemoteMovieSource by tmdbSource
