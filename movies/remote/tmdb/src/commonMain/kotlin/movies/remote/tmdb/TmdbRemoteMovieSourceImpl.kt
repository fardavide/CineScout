package movies.remote.tmdb

import com.soywiz.klock.DateFormat
import com.soywiz.klock.parse
import entities.TmdbId
import entities.TmdbStringId
import entities.field.Actor
import entities.field.CommunityRating
import entities.field.Genre
import entities.field.Name
import entities.field.TmdbImageUrl
import entities.field.Video
import entities.movies.DiscoverParams
import entities.movies.Movie
import io.ktor.client.features.ClientRequestException
import movies.remote.TmdbRemoteMovieSource
import movies.remote.tmdb.model.MovieDetails
import movies.remote.tmdb.model.MoviePageResult
import movies.remote.tmdb.movie.MovieDiscoverService
import movies.remote.tmdb.movie.MovieSearchService
import movies.remote.tmdb.movie.MovieService
import util.mapNotNullAsync
import util.takeIfNotBlank

internal class TmdbRemoteMovieSourceImpl(
    private val movieDiscoverService: MovieDiscoverService,
    private val movieService: MovieService,
    private val movieSearchService: MovieSearchService,
) : TmdbRemoteMovieSource {

    override suspend fun find(id: TmdbId): Movie? =
        runCatching { movieService.details(id.i) }
            .map { it.toBusinessModel() }
            .getOrNull()

    override suspend fun discover(params: DiscoverParams): Collection<Movie> =
        movieDiscoverService.discover(params).toBusinessModels()

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
            poster = ImageUrl(movieModel.posterPath),
            backdrop = ImageUrl(movieModel.backdropPath),
            actors = movieModel.credits.cast.map { castPerson ->
                Actor(
                    id = TmdbId(castPerson.id),
                    name = Name(castPerson.name)
                )
            },
            genres = movieModel.genres.map { genre -> Genre(id = TmdbId(genre.id), name = Name(genre.name)) },
            year = getYear(movieModel.releaseDate),
            rating = CommunityRating(movieModel.voteAverage, movieModel.voteCount.toUInt()),
            overview = movieModel.overview,
            videos = movieModel.videos.results.map { videoResult ->
                Video(
                    id = TmdbStringId( videoResult.id),
                    title = Name(videoResult.name),
                    site = videoResult.site,
                    key = videoResult.key,
                    type = videoResult.type,
                    size = videoResult.size.toUInt()
                )
            }
        )
    }

    private fun getYear(releaseDate: String): UInt {
        val str = releaseDate.takeIfNotBlank() ?: return 0u
        return DateFormat("yyyy-MM-dd").parse(str).yearInt.toUInt()
    }

    private companion object {
        fun ImageUrl(path: String?) = path?.let { TmdbImageUrl(IMAGE_BASE_URL, path) }
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p"
    }
}
