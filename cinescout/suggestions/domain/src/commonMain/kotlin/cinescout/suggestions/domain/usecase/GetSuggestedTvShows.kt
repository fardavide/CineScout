package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import cinescout.suggestions.domain.SuggestionRepository
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.tvshows.domain.TvShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.transformLatest
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import store.Refresh

@Factory
class GetSuggestedTvShows(
    private val suggestionRepository: SuggestionRepository,
    private val tvShowRepository: TvShowRepository,
    private val updateSuggestions: UpdateSuggestions,
    @Named(UpdateIfSuggestionsLessThanName)
    private val updateIfSuggestionsLessThan: Int = DefaultMinimumSuggestions
) {

    operator fun invoke(): Flow<Either<SuggestionError, NonEmptyList<SuggestedTvShow>>> =
        updateSuggestionsTrigger().flatMapLatest {
            suggestionRepository.getSuggestedTvShows().transformLatest { either ->
                either
                    .onRight { tvShows ->
                        emit(tvShows.right())
                        if (tvShows.size < updateIfSuggestionsLessThan) {
                            updateSuggestions(SuggestionsMode.Quick)
                                .onLeft { error -> emit(error.left()) }
                        }
                    }
                    .onLeft {
                        updateSuggestions(SuggestionsMode.Quick)
                            .onLeft { error -> emit(error.left()) }
                    }
            }
        }

    private fun updateSuggestionsTrigger() = combine(
        tvShowRepository.getAllLikedTvShows(),
        tvShowRepository.getAllRatedTvShows(refresh = Refresh.IfNeeded),
        tvShowRepository.getAllWatchlistTvShows(refresh = Refresh.IfNeeded)
    ) { likedTvShows, ratedTvShowsEither, watchlistTvShowsEither ->
        either {
            likedTvShows.isNotEmpty() ||
                ratedTvShowsEither.bind().isNotEmpty() ||
                watchlistTvShowsEither.bind().isNotEmpty()
        }.fold(
            ifLeft = { false },
            ifRight = { it }
        )
    }.distinctUntilChanged()

    companion object {

        const val DefaultMinimumSuggestions = 20
        const val UpdateIfSuggestionsLessThanName = "UpdateIfSuggestionsLessThan"
    }
}
