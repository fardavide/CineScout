package cinescout.suggestions.domain.usecase

import arrow.core.NonEmptyList
import arrow.core.Option
import arrow.core.none
import arrow.core.some
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Genre
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieWithExtras
import cinescout.movies.domain.model.ReleaseYear
import kotlin.random.Random
import kotlin.random.nextInt

class BuildDiscoverMoviesParams(private val shouldIncludeAllTheParam: Boolean = false) {

    private var randomSeed: Int = Random.nextInt(-10..10)
        get() {
            field += Random.nextInt(-10..10)
            return field
        }
    private val random = Random(randomSeed)

    operator fun invoke(positiveMovies: NonEmptyList<MovieWithExtras>): DiscoverMoviesParams {
        val movie = positiveMovies.random()
        return DiscoverMoviesParams(
            castMember = getCastMember(movie),
            crewMember = getCrewMember(movie),
            genre = getGenre(movie),
            keyword = none(), // TODO
            releaseYear = getReleaseYear(movie)
        )
    }

    private fun getCastMember(movie: MovieWithExtras): Option<MovieCredits.CastMember> =
        randomlyInclude { movie.credits.cast.random() }

    private fun getCastMember(positiveMovies: NonEmptyList<MovieWithExtras>): Option<MovieCredits.CastMember> =
        getCastMember(positiveMovies.random())

    private fun getCrewMember(movie: MovieWithExtras): Option<MovieCredits.CrewMember> =
        randomlyInclude { movie.credits.crew.random() }

    private fun getCrewMember(positiveMovies: NonEmptyList<MovieWithExtras>): Option<MovieCredits.CrewMember> =
        getCrewMember(positiveMovies.random())

    private fun getGenre(movie: MovieWithExtras): Option<Genre> =
        randomlyInclude { movie.movieWithDetails.genres.random() }

    private fun getGenre(positiveMovies: NonEmptyList<MovieWithExtras>): Option<Genre> =
        getGenre(positiveMovies.random())

    private fun getReleaseYear(movie: MovieWithExtras): Option<ReleaseYear> =
        randomlyInclude { ReleaseYear(movie.movieWithDetails.movie.releaseDate.year) }

    private fun getReleaseYear(positiveMovies: NonEmptyList<MovieWithExtras>): Option<ReleaseYear> =
        getReleaseYear(positiveMovies.random())

    private fun <T> randomlyInclude(f: () -> T): Option<T> =
        if (shouldIncludeAllTheParam || random.nextInt() % 3 == 0) f().some() else none()
}
