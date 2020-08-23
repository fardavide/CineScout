package movies.remote.tmdb

import com.soywiz.klock.DateFormat
import com.soywiz.klock.parse
import entities.*
import entities.movies.Movie
import entities.util.mapNotNullAsync
import entities.util.takeIfNotBlank
import io.ktor.client.features.*
import movies.remote.TmdbRemoteMovieSource
import movies.remote.tmdb.model.MovieDetails
import movies.remote.tmdb.model.MoviePageResult
import movies.remote.tmdb.movie.MovieDiscoverService
import movies.remote.tmdb.movie.MovieSearchService
import movies.remote.tmdb.movie.MovieService

internal class TmdbRemoteMovieSourceImpl(
    private val movieDiscoverService: MovieDiscoverService,
    private val movieService: MovieService,
    private val movieSearchService: MovieSearchService,
) : TmdbRemoteMovieSource {

    override suspend fun find(id: TmdbId): Movie? =
        runCatching { movieService.details(id.i) }
            .map { it.toBusinessModel() }
            .getOrNull()

    override suspend fun discover(
        actors: Collection<Actor>,
        genres: Collection<Genre>,
        years: FiveYearRange?,
    ): Collection<Movie> =
        movieDiscoverService.discover(actors, genres, years).toBusinessModels()

    override suspend fun search(query: String): Collection<Movie> =
        if (query.isBlank()) emptyList()
        else movieSearchService.search(query).toBusinessModels()

    private suspend fun MoviePageResult.toBusinessModels(): List<Movie> =
        results.mapNotNullAsync {
            try {
                movieService.details(it.id)
            } catch (e: ClientRequestException) {
                if (e.response?.status?.value != 404) throw e
                null
            }
        }.map { it.toBusinessModel() }

    private fun MovieDetails.toBusinessModel(): Movie {
        val movieModel = this
        return Movie(
            id = TmdbId(movieModel.id),
            name = Name(movieModel.originalTitle),
            actors = movieModel.credits.cast.map { castPerson ->
                Actor(
                    id = TmdbId(castPerson.id),
                    name = Name(castPerson.name)
                )
            },
            genres = movieModel.genres.map { genre -> Genre(id = TmdbId(genre.id), name = Name(genre.name)) },
            year = getYear(movieModel.releaseDate)
        )
    }

    private fun getYear(releaseDate: String): UInt {
        val str = releaseDate.takeIfNotBlank() ?: return 0u
        return DateFormat("yyyy-MM-dd").parse(str).yearInt.toUInt()
    }
}
