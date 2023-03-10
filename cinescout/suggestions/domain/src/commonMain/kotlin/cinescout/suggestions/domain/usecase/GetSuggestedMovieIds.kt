package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import cinescout.movies.domain.MovieRepository
import cinescout.store5.ext.filterData
import cinescout.suggestions.domain.SuggestionRepository
import cinescout.suggestions.domain.model.SuggestedMovieId
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionsMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.transformLatest
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
class GetSuggestedMovieIds(
    private val movieRepository: MovieRepository,
    private val suggestionRepository: SuggestionRepository,
    private val updateSuggestions: UpdateSuggestions,
    @Named(UpdateIfSuggestionsLessThanName)
    private val updateIfSuggestionsLessThan: Int = DefaultMinimumSuggestions
) {

    operator fun invoke(): Flow<Either<SuggestionError, NonEmptyList<SuggestedMovieId>>> =
        updateSuggestionsTrigger().flatMapLatest {
            suggestionRepository.getSuggestedMovieIds().transformLatest { either ->
                either
                    .onRight { movies ->
                        emit(movies.right())
                        if (movies.size < updateIfSuggestionsLessThan) {
                            updateSuggestions(SuggestionsMode.Quick)
                                .onLeft { error -> emit(SuggestionError.Source(error).left()) }
                        }
                    }
                    .onLeft {
                        updateSuggestions(SuggestionsMode.Quick)
                            .onLeft { error -> emit(SuggestionError.Source(error).left()) }
                    }
            }
        }

    private fun updateSuggestionsTrigger() = combine(
        movieRepository.getAllLikedMovies(),
        movieRepository.getAllRatedMovies(refresh = false).filterData(),
        movieRepository.getAllWatchlistMovies(refresh = false).filterData()
    ) { likedMovies, ratedMoviesEither, watchlistMoviesEither ->
        either {
            likedMovies.isNotEmpty() ||
                ratedMoviesEither.bind().isNotEmpty() ||
                watchlistMoviesEither.bind().isNotEmpty()
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
