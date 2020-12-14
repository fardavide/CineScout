package movies.remote.tmdb

import entities.Either
import entities.NetworkError
import entities.ResourceError
import entities.TmdbId
import entities.invoke
import entities.left
import entities.movies.DiscoverParams
import entities.movies.Movie
import entities.movies.SearchError
import entities.or
import movies.remote.TmdbRemoteMovieSource
import movies.remote.tmdb.mapper.MovieDetailsMapper
import movies.remote.tmdb.mapper.MoviePageResultMapper
import movies.remote.tmdb.mapper.flatMap
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

    override suspend fun find(id: TmdbId): Either<ResourceError, Movie> = movieService.details(id.i)
        .flatMap(movieDetailsMapper) { it.toBusinessModel() } or ResourceError::Network

    override suspend fun discover(params: DiscoverParams): Either<NetworkError, List<Movie>> =
        moviePageResultMapper { movieDiscoverService.discover(params).toBusinessModel() }

    override suspend fun search(query: String): Either<SearchError, List<Movie>> =
        when (query.trim().length) {
            0 -> SearchError.EmptyQuery.left()
            1 -> SearchError.ShortQuery.left()
            else -> moviePageResultMapper { movieSearchService.search(query).toBusinessModel() } or SearchError::Network
        }
}
