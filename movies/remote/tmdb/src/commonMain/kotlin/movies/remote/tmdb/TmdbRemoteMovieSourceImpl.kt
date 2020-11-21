package movies.remote.tmdb

import entities.TmdbId
import entities.invoke
import entities.movies.DiscoverParams
import entities.movies.Movie
import movies.remote.TmdbRemoteMovieSource
import movies.remote.tmdb.mapper.MovieDetailsMapper
import movies.remote.tmdb.mapper.MoviePageResultMapper
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

    override suspend fun find(id: TmdbId): Movie? =
        runCatching { movieService.detailsOrThrow(id.i) }
            .mapOrNull(movieDetailsMapper) { it.toBusinessModel() }

    override suspend fun discover(params: DiscoverParams): Collection<Movie> =
        moviePageResultMapper { movieDiscoverService.discover(params).toBusinessModel() }

    override suspend fun search(query: String): Collection<Movie> =
        if (query.isBlank()) emptyList()
        else moviePageResultMapper { movieSearchService.search(query).toBusinessModel() }

}
