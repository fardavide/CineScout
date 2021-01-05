package movies.remote.tmdb.mapper

import com.soywiz.klock.DateFormat
import com.soywiz.klock.parse
import entities.Either
import entities.NetworkError
import entities.TmdbId
import entities.TmdbStringId
import entities.model.Actor
import entities.model.CommunityRating
import entities.model.Genre
import entities.model.Name
import entities.model.TmdbImageUrl
import entities.model.Video
import entities.movies.Movie
import entities.right
import movies.remote.tmdb.model.MovieDetails
import util.takeIfNotBlank

class MovieDetailsMapper : Mapper<MovieDetails, Movie> {

    override suspend fun MovieDetails.toBusinessModel(): Either<NetworkError, Movie> {
        return Movie(
            id = TmdbId(id),
            name = Name(title),
            poster = ImageUrl(posterPath),
            backdrop = ImageUrl(backdropPath),
            actors = credits.cast.map { castPerson ->
                Actor(
                    id = TmdbId(castPerson.id),
                    name = Name(castPerson.name)
                )
            },
            genres = genres.map { genre -> Genre(id = TmdbId(genre.id), name = Name(genre.name)) },
            year = getYear(releaseDate),
            rating = CommunityRating(voteAverage, voteCount.toUInt()),
            popularity = popularity,
            overview = overview,
            videos = videos.results.map { videoResult ->
                Video(
                    id = TmdbStringId( videoResult.id),
                    title = Name(videoResult.name),
                    site = videoResult.site,
                    key = videoResult.key,
                    type = videoResult.type,
                    size = videoResult.size.toUInt()
                )
            }
        ).right()
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
