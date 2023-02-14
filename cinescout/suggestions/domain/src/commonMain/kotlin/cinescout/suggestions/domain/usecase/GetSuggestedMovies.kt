package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import cinescout.common.model.SuggestionError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.suggestions.domain.model.SuggestionsMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.transformLatest
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import store.Refresh

@Factory
class GetSuggestedMovies(
    private val movieRepository: MovieRepository,
    private val updateSuggestedMovies: UpdateSuggestedMovies,
    @Named(UpdateIfSuggestionsLessThanName)
    private val updateIfSuggestionsLessThan: Int = DefaultMinimumSuggestions
) {

    operator fun invoke(): Flow<Either<SuggestionError, NonEmptyList<Movie>>> =
        updateSuggestionsTrigger().flatMapLatest {
            movieRepository.getSuggestedMovies().transformLatest { either ->
                either
                    .onRight { movies ->
                        emit(movies.right())
                        if (movies.size < updateIfSuggestionsLessThan) {
                            updateSuggestedMovies(SuggestionsMode.Quick)
                                .onLeft { error -> emit(error.left()) }
                        }
                    }
                    .onLeft {
                        updateSuggestedMovies(SuggestionsMode.Quick)
                            .onLeft { error -> emit(error.left()) }
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
        const val UpdateIfSuggestionsLessThanName = "updateIfSuggestionsLessThan"
    }
}
