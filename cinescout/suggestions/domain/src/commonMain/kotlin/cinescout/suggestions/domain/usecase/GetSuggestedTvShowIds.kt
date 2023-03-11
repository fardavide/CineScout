package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.store5.ext.filterData
import cinescout.suggestions.domain.SuggestionRepository
import cinescout.suggestions.domain.model.SuggestedTvShowId
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowIdWithPersonalRating
import cinescout.tvshows.domain.store.RatedTvShowIdsStore
import cinescout.tvshows.domain.store.WatchlistTvShowIdsStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.transformLatest
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import org.mobilenativefoundation.store.store5.StoreReadRequest

@Factory
class GetSuggestedTvShowIds(
    private val ratedTvShowIdsStore: RatedTvShowIdsStore,
    private val suggestionRepository: SuggestionRepository,
    private val tvShowRepository: TvShowRepository,
    private val updateSuggestions: UpdateSuggestions,
    @Named(UpdateIfSuggestionsLessThanName)
    private val updateIfSuggestionsLessThan: Int = DefaultMinimumSuggestions,
    private val watchlistTvShowIdsStore: WatchlistTvShowIdsStore
) {

    operator fun invoke(): Flow<Either<SuggestionError, NonEmptyList<SuggestedTvShowId>>> =
        updateSuggestionsTrigger().flatMapLatest {
            suggestionRepository.getSuggestedTvShowIds().transformLatest { either ->
                either
                    .onRight { tvShows ->
                        emit(tvShows.right())
                        if (tvShows.size < updateIfSuggestionsLessThan) {
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
        tvShowRepository.getAllLikedTvShows(),
        ratedTvShows(),
        watchlistTvShows()
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

    private fun ratedTvShows(): Flow<Either<NetworkError, List<TvShowIdWithPersonalRating>>> =
        ratedTvShowIdsStore.stream(StoreReadRequest.cached(Unit, refresh = false)).filterData()

    private fun watchlistTvShows(): Flow<Either<NetworkError, List<TmdbTvShowId>>> =
        watchlistTvShowIdsStore.stream(StoreReadRequest.cached(Unit, refresh = false)).filterData()

    companion object {

        const val DefaultMinimumSuggestions = 20
        const val UpdateIfSuggestionsLessThanName = "UpdateIfSuggestionsLessThan"
    }
}
