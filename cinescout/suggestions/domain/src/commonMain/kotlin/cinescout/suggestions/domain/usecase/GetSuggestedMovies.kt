package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionsMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.transformLatest
import store.Refresh

class GetSuggestedMovies(
    private val movieRepository: MovieRepository,
    private val updateSuggestedMovies: UpdateSuggestedMovies,
    private val updateIfSuggestionsLessThan: Int = DefaultMinimumSuggestions
) {

    operator fun invoke(): Flow<Either<SuggestionError, NonEmptyList<Movie>>> =
        updateSuggestionsTrigger().flatMapLatest {
            movieRepository.getSuggestedMovies().transformLatest { either ->
                either
                    .tap { movies ->
                        emit(movies.right())
                        if (movies.size < updateIfSuggestionsLessThan) {
                            updateSuggestedMovies(SuggestionsMode.Quick)
                                .tapLeft { error -> emit(error.left()) }
                        }
                    }
                    .tapLeft {
                        updateSuggestedMovies(SuggestionsMode.Quick)
                            .tapLeft { error -> emit(error.left()) }
                    }
            }
        }

    private fun updateSuggestionsTrigger() = combine(
        movieRepository.getAllLikedMovies(),
        movieRepository.getAllRatedMovies(refresh = Refresh.IfNeeded),
        movieRepository.getAllWatchlistMovies(refresh = Refresh.IfNeeded)
    ) { likedMovies, ratedMoviesEither, watchlistMoviesEither ->
        either {
            likedMovies.isNotEmpty() ||
                ratedMoviesEither.bind().data.isNotEmpty() ||
                watchlistMoviesEither.bind().data.isNotEmpty()
        }.fold(
            ifLeft = { false },
            ifRight = { it }
        )
    }.distinctUntilChanged()

    companion object {

        const val DefaultMinimumSuggestions = 20
    }
}
