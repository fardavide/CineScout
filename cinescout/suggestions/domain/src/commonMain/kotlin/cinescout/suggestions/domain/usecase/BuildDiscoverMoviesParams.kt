package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.Option
import arrow.core.continuations.either
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.ReleaseYear
import cinescout.movies.domain.model.SuggestionError

class BuildDiscoverMoviesParams {

    suspend operator fun invoke(
        ratedMovies: List<MovieWithPersonalRating>
    ): Either<SuggestionError, DiscoverMoviesParams> =
        either {
            val positivelyRatedMovies = ratedMovies.filterPositiveRating()
                .toEither { SuggestionError.NoSuggestions }
                .bind()

            buildParams(positivelyRatedMovies)
        }

    private fun List<MovieWithPersonalRating>.filterPositiveRating(): Option<NonEmptyList<Movie>> {
        val filtered = filter { it.rating.value >= 7 }
            .map { it.movie }
        return NonEmptyList.fromList(filtered)
    }

    private fun buildParams(ratedMovies: NonEmptyList<Movie>) = DiscoverMoviesParams(
        releaseYear = ReleaseYear(ratedMovies.random().releaseDate.year)
    )
}
