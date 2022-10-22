package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import cinescout.common.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TvShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.transformLatest
import store.Refresh

class GetSuggestedTvShows(
    private val tvShowRepository: TvShowRepository,
    private val updateSuggestedTvShows: UpdateSuggestedTvShows,
    private val updateIfSuggestionsLessThan: Int = DefaultMinimumSuggestions
) {

    operator fun invoke(): Flow<Either<SuggestionError, NonEmptyList<TvShow>>> =
        updateSuggestionsTrigger().flatMapLatest {
            tvShowRepository.getSuggestedTvShows().transformLatest { either ->
                either
                    .tap { movies ->
                        emit(movies.right())
                        if (movies.size < updateIfSuggestionsLessThan) {
                            updateSuggestedTvShows(SuggestionsMode.Quick)
                                .tapLeft { error -> emit(error.left()) }
                        }
                    }
                    .tapLeft {
                        updateSuggestedTvShows(SuggestionsMode.Quick)
                            .tapLeft { error -> emit(error.left()) }
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
                ratedTvShowsEither.bind().data.isNotEmpty() ||
                watchlistTvShowsEither.bind().data.isNotEmpty()
        }.fold(
            ifLeft = { false },
            ifRight = { it }
        )
    }.distinctUntilChanged()

    companion object {

        const val DefaultMinimumSuggestions = 20
    }
}
