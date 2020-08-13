package movies

import entities.movies.MovieRepository

internal class MovieRepositoryImpl(
    private val remoteSource: RemoteMovieSource
) : MovieRepository by remoteSource
