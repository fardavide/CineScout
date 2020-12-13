package movies.remote.tmdb

import entities.Either
import entities.NetworkError
import entities.ResourceError
import entities.TmdbId
import entities.invoke
import entities.movies.DiscoverParams
import entities.movies.Movie
import entities.or
import movies.remote.TmdbRemoteMovieSource
import movies.remote.tmdb.mapper.MovieDetailsMapper
import movies.remote.tmdb.mapper.MoviePageResultMapper
import movies.remote.tmdb.mapper.map
import movies.remote.tmdb.mapper.mapOrNull
import movies.remote.tmdb.movie.MovieDiscoverService
import movies.remote.tmdb.movie.MovieSearchService
import movies.remote.tmdb.movie.MovieService

internal class TmdbRemoteMovieSourceImpl(
    private val movieDiscoverService: MovieDiscoverService,
    private val movieService: MovieService,
    private val movieSearchService: MovieSearchService,
    private val moviePageResultMapper: MoviePageResultMapper,
    private val movieDetailsMapper: MovieDetailsMapper
) : TmdbRemoteMovieSource {

    override suspend fun find(id: TmdbId): Either<ResourceError, Movie> =
        movieService.details(id.i)
            .map(movieDetailsMapper) { it.toBusinessModel() } or ResourceError::Network

    override suspend fun discover(params: DiscoverParams): Collection<Movie> =
        moviePageResultMapper { movieDiscoverService.discover(params).toBusinessModel() }

    override suspend fun search(query: String): Collection<Movie> =
        if (query.isBlank()) emptyList()
        else moviePageResultMapper { movieSearchService.search(query).toBusinessModel() }

}
