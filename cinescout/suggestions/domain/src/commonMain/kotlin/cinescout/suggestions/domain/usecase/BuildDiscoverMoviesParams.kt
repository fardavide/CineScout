package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.Option
import arrow.core.continuations.either
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithRating
import cinescout.movies.domain.usecase.GetAllRatedMovies
import cinescout.suggestions.domain.model.DiscoverMoviesParams
import cinescout.suggestions.domain.model.NoSuggestions
import cinescout.suggestions.domain.model.ReleaseYear
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BuildDiscoverMoviesParams(
    private val getAllRatedMovies: GetAllRatedMovies
) {

    operator fun invoke(): Flow<Either<NoSuggestions, DiscoverMoviesParams>> =
        getAllRatedMovies().map { ratedMoviesEither ->
            either {
                val ratedMovies = ratedMoviesEither
                    .mapLeft { NoSuggestions }
                    .bind()

                val positivelyRatedMovies = ratedMovies.filterPositiveRating()
                    .toEither { NoSuggestions }
                    .bind()

                buildParams(positivelyRatedMovies)
            }
        }

    private fun List<MovieWithRating>.filterPositiveRating(): Option<NonEmptyList<Movie>> {
        val filtered = filter { it.rating.value >= 7 }
            .map { it.movie }
        return NonEmptyList.fromList(filtered)
    }

    private fun buildParams(ratedMovies: NonEmptyList<Movie>) = DiscoverMoviesParams(
        releaseYear = ReleaseYear(ratedMovies.random().releaseDate.year)
    )
}
