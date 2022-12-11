package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.left
import arrow.core.right
import cinescout.common.model.SuggestionError
import cinescout.error.DataError
import cinescout.tvshows.domain.model.TvShowWithExtras
import cinescout.tvshows.domain.usecase.GetTvShowExtras
import cinescout.utils.kotlin.combineToLazyList
import cinescout.utils.kotlin.nonEmptyUnsafe
import cinescout.utils.kotlin.shiftWithAnyRight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import store.Refresh

@Factory
class GetSuggestedTvShowsWithExtras(
    private val getSuggestedTvShows: GetSuggestedTvShows,
    private val getTvShowExtras: GetTvShowExtras
) {

    operator fun invoke(
        tvShowExtraRefresh: Refresh = Refresh.IfExpired(),
        take: Int = Integer.MAX_VALUE
    ): Flow<Either<SuggestionError, NonEmptyList<TvShowWithExtras>>> =
        getSuggestedTvShows().flatMapLatest { either ->
            either.fold(
                ifLeft = { suggestionError -> flowOf(suggestionError.left()) },
                ifRight = { tvShows ->
                    tvShows.take(take).map { tvShow -> getTvShowExtras(tvShow, refresh = tvShowExtraRefresh) }
                        .combineToLazyList()
                        .map { either ->
                            either.shiftWithAnyRight().fold(
                                ifLeft = { dataError -> SuggestionError.Source(dataError as DataError.Remote).left() },
                                ifRight = { it.nonEmptyUnsafe().right() }
                            )
                        }
                }
            )
        }
}
