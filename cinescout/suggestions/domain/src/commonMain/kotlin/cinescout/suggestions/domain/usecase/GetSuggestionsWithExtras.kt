@file:Suppress("BOUNDS_NOT_ALLOWED_IF_BOUNDED_BY_TYPE_PARAMETER")

package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptyList
import arrow.core.flatMap
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import cinescout.details.domain.model.Extra
import cinescout.details.domain.model.WithExtra
import cinescout.details.domain.usecase.GetExtra
import cinescout.error.NetworkError
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.media.domain.model.ScreenplayMedia
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.rating.domain.model.PersonalRating
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.store.ScreenplayStore
import cinescout.store5.ext.filterData
import cinescout.suggestions.domain.model.SuggestedScreenplayId
import cinescout.suggestions.domain.model.SuggestedScreenplayWithExtra
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.model.WithSuggestedScreenplay
import cinescout.utils.kotlin.combineToLazyList
import cinescout.utils.kotlin.exhaustive
import cinescout.utils.kotlin.nonEmpty
import cinescout.utils.kotlin.nonEmptyUnsafe
import cinescout.utils.kotlin.shiftWithAnyRight
import cinescout.watchlist.domain.model.IsInWatchlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface GetSuggestionsWithExtras {

    operator fun <S1 : WithExtra, SR> invoke(
        type: ScreenplayTypeFilter,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        take: Int = Integer.MAX_VALUE
    ): Flow<Either<SuggestionError, Nel<SR>>> where SR : WithSuggestedScreenplay, SR : WithExtra, SR : S1

    operator fun <S1 : WithExtra, S2 : WithExtra, SR> invoke(
        type: ScreenplayTypeFilter,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>,
        take: Int = Integer.MAX_VALUE
    ): Flow<Either<SuggestionError, Nel<SR>>> where SR : WithSuggestedScreenplay, SR : WithExtra, SR : S1, SR : S2

    operator fun <S1 : WithExtra, S2 : WithExtra, S3 : WithExtra, SR> invoke(
        type: ScreenplayTypeFilter,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>,
        e3: Extra<S3>,
        take: Int = Integer.MAX_VALUE
    ): Flow<Either<SuggestionError, Nel<SR>>> where SR : WithSuggestedScreenplay,
          SR : WithExtra,
          SR : S1,
          SR : S2,
          SR : S3

    operator fun <S1 : WithExtra, S2 : WithExtra, S3 : WithExtra, S4 : WithExtra, SR> invoke(
        type: ScreenplayTypeFilter,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>,
        e3: Extra<S3>,
        e4: Extra<S4>,
        take: Int = Integer.MAX_VALUE
    ): Flow<Either<SuggestionError, Nel<SR>>> where SR : WithSuggestedScreenplay,
          SR : WithExtra,
          SR : S1,
          SR : S2,
          SR : S3,
          SR : S4
}

@Factory
class RealGetSuggestionsWithExtras(
    private val getExtra: GetExtra,
    private val getSuggestionIds: GetSuggestionIds,
    private val screenplayStore: ScreenplayStore
) : GetSuggestionsWithExtras {

    override fun <S1 : WithExtra, SR> invoke(
        type: ScreenplayTypeFilter,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        take: Int
    ): Flow<Either<SuggestionError, Nel<SR>>> where SR : WithSuggestedScreenplay, SR : WithExtra, SR : S1 =
        getSuggestions(type, take) { suggestionId ->
            val e1Flow = getExtra(suggestionId.screenplayIds, e1, refreshExtras)
            combine(
                getScreenplay(suggestionId.screenplayIds, refresh),
                e1Flow
            ) { screenplayEither, e1Either ->
                either {
                    val screenplay = screenplayEither.bind()
                    val extra1 = e1Either.bind()
                    @Suppress("UNCHECKED_CAST")
                    SuggestedScreenplayWithExtra(
                        affinity = suggestionId.affinity,
                        screenplay = screenplay,
                        source = suggestionId.source
                    ).applyExtras(extra1) as SR
                }
            }
        }

    override fun <S1 : WithExtra, S2 : WithExtra, SR> invoke(
        type: ScreenplayTypeFilter,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>,
        take: Int
    ): Flow<Either<SuggestionError, Nel<SR>>> where SR : WithSuggestedScreenplay, SR : WithExtra, SR : S1, SR : S2 =
        getSuggestions(type, take) { suggestionId ->
            val e1Flow = getExtra(suggestionId.screenplayIds, e1, refreshExtras)
            val e2Flow = getExtra(suggestionId.screenplayIds, e2, refreshExtras)
            combine(
                getScreenplay(suggestionId.screenplayIds, refresh),
                e1Flow,
                e2Flow
            ) { screenplayEither, e1Either, e2Either ->
                either {
                    val screenplay = screenplayEither.bind()
                    val extra1 = e1Either.bind()
                    val extra2 = e2Either.bind()
                    @Suppress("UNCHECKED_CAST")
                    SuggestedScreenplayWithExtra(
                        affinity = suggestionId.affinity,
                        screenplay = screenplay,
                        source = suggestionId.source
                    ).applyExtras(extra1, extra2) as SR
                }
            }
        }

    override fun <S1 : WithExtra, S2 : WithExtra, S3 : WithExtra, SR> invoke(
        type: ScreenplayTypeFilter,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>,
        e3: Extra<S3>,
        take: Int
    ): Flow<Either<SuggestionError, Nel<SR>>> where SR : WithSuggestedScreenplay,
          SR : WithExtra, SR : S1,
          SR : S2,
          SR : S3 =
        getSuggestions(type, take) { suggestionId ->
            val e1Flow = getExtra(suggestionId.screenplayIds, e1, refreshExtras)
            val e2Flow = getExtra(suggestionId.screenplayIds, e2, refreshExtras)
            val e3Flow = getExtra(suggestionId.screenplayIds, e3, refreshExtras)
            combine(
                getScreenplay(suggestionId.screenplayIds, refresh),
                e1Flow,
                e2Flow,
                e3Flow
            ) { screenplayEither, e1Either, e2Either, e3Either ->
                either {
                    val screenplay = screenplayEither.bind()
                    val extra1 = e1Either.bind()
                    val extra2 = e2Either.bind()
                    val extra3 = e3Either.bind()
                    @Suppress("UNCHECKED_CAST")
                    SuggestedScreenplayWithExtra(
                        affinity = suggestionId.affinity,
                        screenplay = screenplay,
                        source = suggestionId.source
                    ).applyExtras(extra1, extra2, extra3) as SR
                }
            }
        }

    override fun <S1 : WithExtra, S2 : WithExtra, S3 : WithExtra, S4 : WithExtra, SR> invoke(
        type: ScreenplayTypeFilter,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>,
        e3: Extra<S3>,
        e4: Extra<S4>,
        take: Int
    ): Flow<Either<SuggestionError, Nel<SR>>> where SR : WithSuggestedScreenplay, SR : WithExtra,
          SR : S1,
          SR : S2,
          SR : S3,
          SR : S4 =
        getSuggestions(type, take) { suggestionId ->
            val e1Flow = getExtra(suggestionId.screenplayIds, e1, refreshExtras)
            val e2Flow = getExtra(suggestionId.screenplayIds, e2, refreshExtras)
            val e3Flow = getExtra(suggestionId.screenplayIds, e3, refreshExtras)
            val e4Flow = getExtra(suggestionId.screenplayIds, e4, refreshExtras)
            combine(
                getScreenplay(suggestionId.screenplayIds, refresh),
                e1Flow,
                e2Flow,
                e3Flow,
                e4Flow
            ) { screenplayEither, e1Either, e2Either, e3Either, e4Either ->
                either {
                    val screenplay = screenplayEither.bind()
                    val extra1 = e1Either.bind()
                    val extra2 = e2Either.bind()
                    val extra3 = e3Either.bind()
                    val extra4 = e4Either.bind()
                    @Suppress("UNCHECKED_CAST")
                    SuggestedScreenplayWithExtra(
                        affinity = suggestionId.affinity,
                        screenplay = screenplay,
                        source = suggestionId.source
                    ).applyExtras(extra1, extra2, extra3, extra4) as SR
                }
            }
        }

    private fun <T> getSuggestions(
        type: ScreenplayTypeFilter,
        take: Int,
        withExtraFlowBuilder: (SuggestedScreenplayId) -> Flow<Either<NetworkError, T>>
    ): Flow<Either<SuggestionError, NonEmptyList<T>>> where T : WithSuggestedScreenplay, T : WithExtra =
        getSuggestionIds(type).flatMapLatest { either ->
            either.fold(
                ifLeft = { suggestionError -> flowOf(suggestionError.left()) },
                ifRight = { suggestionIds ->
                    suggestionIds.take(take).map(withExtraFlowBuilder).combineToLazyList()
                        .map { either ->
                            either.shiftWithAnyRight().fold(
                                ifLeft = { networkError -> SuggestionError.Source(networkError).left() },
                                ifRight = { it.nonEmptyUnsafe().right() }
                            )
                        }
                }
            )
        }

    private fun getScreenplay(
        screenplayIds: ScreenplayIds,
        refresh: Boolean
    ): Flow<Either<NetworkError, Screenplay>> = screenplayStore
        .stream(StoreReadRequest.cached(screenplayIds, refresh))
        .filterData()
        .map { either -> either.map { screenplayWithGenreSlugs -> screenplayWithGenreSlugs.screenplay } }

    private fun SuggestedScreenplayWithExtra.applyExtras(vararg extras: Any): SuggestedScreenplayWithExtra =
        apply {
            for (extra in extras) {
                when (extra) {
                    is IsInWatchlist -> isInWatchlistBoxed = extra
                    is PersonalRating -> personalRatingBoxed = extra
                    is ScreenplayCredits -> credits = extra
                    is ScreenplayGenres -> genres = extra
                    is ScreenplayHistory -> history = extra
                    is ScreenplayKeywords -> keywords = extra
                    is ScreenplayMedia -> media = extra
                    else -> throw IllegalArgumentException("Unknown extra: $extra")
                }.exhaustive
            }
        }
}

@Suppress("UNCHECKED_CAST")
class FakeGetSuggestionsWithExtras(
    suggestions: NonEmptyList<SuggestedScreenplayWithExtra>? = null,
    suggestionsEither: Either<SuggestionError, NonEmptyList<SuggestedScreenplayWithExtra>> =
        suggestions?.right() ?: SuggestionError.Source(NetworkError.Unknown).left(),
    private val suggestionsEitherFlow: Flow<Either<SuggestionError, NonEmptyList<SuggestedScreenplayWithExtra>>> =
        flowOf(suggestionsEither)
) : GetSuggestionsWithExtras {

    override fun <S1 : WithExtra, SR> invoke(
        type: ScreenplayTypeFilter,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        take: Int
    ): Flow<Either<SuggestionError, Nel<SR>>> where SR : WithSuggestedScreenplay, SR : WithExtra, SR : S1 =
        flow(type)

    override fun <S1 : WithExtra, S2 : WithExtra, SR> invoke(
        type: ScreenplayTypeFilter,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>,
        take: Int
    ): Flow<Either<SuggestionError, Nel<SR>>> where SR : WithSuggestedScreenplay, SR : WithExtra, SR : S1, SR : S2 =
        flow(type)

    override fun <S1 : WithExtra, S2 : WithExtra, S3 : WithExtra, SR> invoke(
        type: ScreenplayTypeFilter,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>,
        e3: Extra<S3>,
        take: Int
    ): Flow<Either<SuggestionError, Nel<SR>>> where SR : WithSuggestedScreenplay,
          SR : WithExtra,
          SR : S1,
          SR : S2,
          SR : S3 =
        flow(type)

    override fun <S1 : WithExtra, S2 : WithExtra, S3 : WithExtra, S4 : WithExtra, SR> invoke(
        type: ScreenplayTypeFilter,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>,
        e3: Extra<S3>,
        e4: Extra<S4>,
        take: Int
    ): Flow<Either<SuggestionError, Nel<SR>>> where SR : WithSuggestedScreenplay,
          SR : WithExtra,
          SR : S1,
          SR : S2,
          SR : S3,
          SR : S4 =
        flow(type)
    
    private fun <SR> flow(type: ScreenplayTypeFilter): Flow<Either<SuggestionError, NonEmptyList<SR>>> =
        suggestionsEitherFlow.map { either -> either.flatMap { it.filterByType(type) } }
            as Flow<Either<SuggestionError, Nel<SR>>>


    private fun Nel<SuggestedScreenplayWithExtra>.filterByType(type: ScreenplayTypeFilter) = when (type) {
        ScreenplayTypeFilter.All -> this
        ScreenplayTypeFilter.Movies -> filter { it.screenplay is Movie }
        ScreenplayTypeFilter.TvShows -> filter { it.screenplay is TvShow }
    }.nonEmpty { SuggestionError.NoSuggestions }
}
