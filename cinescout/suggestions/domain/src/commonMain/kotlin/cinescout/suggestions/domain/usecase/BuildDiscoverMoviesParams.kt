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
import cinescout.movies.domain.usecase.GetAllRatedMovies
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BuildDiscoverMoviesParams(
    private val getAllRatedMovies: GetAllRatedMovies
) {

    operator fun invoke(): Flow<Either<SuggestionError, DiscoverMoviesParams>> =
        getAllRatedMovies().loadAll().map { ratedMoviesEither ->
            either {
                val ratedMovies = ratedMoviesEither
                    .mapLeft(SuggestionError::Source)
                    .bind()

                val positivelyRatedMovies = ratedMovies.data.filterPositiveRating()
                    .toEither { SuggestionError.NoSuggestions }
                    .bind()

                buildParams(positivelyRatedMovies)
            }
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
