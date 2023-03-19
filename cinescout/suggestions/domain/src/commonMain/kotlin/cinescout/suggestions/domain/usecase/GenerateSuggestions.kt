package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.flatMap
import arrow.core.getOrElse
import arrow.core.left
import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.rating.domain.model.ids
import cinescout.rating.domain.usecase.GetPersonalRatingIds
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.ids
import cinescout.screenplay.domain.store.ScreenplayStore
import cinescout.screenplay.domain.store.SimilarScreenplaysStore
import cinescout.store5.ext.filterData
import cinescout.suggestions.domain.model.SuggestedScreenplay
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionIdSource
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.utils.kotlin.nonEmpty
import cinescout.utils.kotlin.randomOrNone
import cinescout.voting.domain.usecase.GetAllDislikedScreenplays
import cinescout.voting.domain.usecase.GetAllLikedScreenplays
import cinescout.watchlist.domain.usecase.GetWatchlistIds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface GenerateSuggestions {

    operator fun invoke(
        type: ScreenplayType,
        suggestionsMode: SuggestionsMode
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedScreenplay>>>
}

@Factory
class RealGenerateSuggestions(
    private val getAllDislikedScreenplays: GetAllDislikedScreenplays,
    private val getAllLikedScreenplays: GetAllLikedScreenplays,
    private val getPersonalRatingIds: GetPersonalRatingIds,
    private val similarScreenplaysStore: SimilarScreenplaysStore,
    private val getWatchlistIds: GetWatchlistIds,
    private val screenplayStore: ScreenplayStore
) : GenerateSuggestions {

    override operator fun invoke(
        type: ScreenplayType,
        suggestionsMode: SuggestionsMode
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedScreenplay>>> = combine(
        getAllDislikedScreenplays(type),
        getAllLikedScreenplays(type),
        getPersonalRatingIds(type, refresh = shouldRefresh(suggestionsMode)).filterData(),
        getWatchlistIds(type, refresh = shouldRefresh(suggestionsMode)).filterData()
    ) { disliked, liked, ratedEither, watchlistEither ->

        val rated = ratedEither
            .getOrElse { emptyList() }

        val watchlist = watchlistEither
            .getOrElse { emptyList() }

        val positive = liked.withLikedSource() + rated.withRatedSource() + watchlist.withWatchlistSource()
        val (sourceId, source) = positive.randomOrNone().map { suggestionIdSource ->
            val sourceTitle = screenplayStore.get(suggestionIdSource.sourceId)
                .getOrElse { return@combine SuggestionError.Source(it).left() }
                .title
            suggestionIdSource.sourceId to suggestionIdSource.toSuggestionSource(sourceTitle)
        }.getOrElse { return@combine SuggestionError.NoSuggestions.left() }

        similarScreenplaysStore.fresh(sourceId).mapLeft { SuggestionError.Source(it) }.flatMap { similarScreenplays ->
            val suggestedScreenplays = similarScreenplays.map { SuggestedScreenplay(it, source) }
            val allKnownScreenplayIds = disliked.ids() + liked.ids() + rated.ids() + watchlist
            suggestedScreenplays.filterKnown(allKnownScreenplayIds)
        }
    }
    
    private fun shouldRefresh(suggestionsMode: SuggestionsMode) = when (suggestionsMode) {
        SuggestionsMode.Deep -> true
        SuggestionsMode.Quick -> false
    }

    private fun List<Screenplay>.withLikedSource(): List<SuggestionIdSource> =
        map { SuggestionIdSource.Liked(sourceId = it.tmdbId) }

    private fun List<TmdbScreenplayId>.withWatchlistSource(): List<SuggestionIdSource> =
        map { SuggestionIdSource.Watchlist(sourceId = it) }
    
    private fun List<ScreenplayIdWithPersonalRating>.withRatedSource(): List<SuggestionIdSource> =
        filter { it.personalRating.value >= 6 }
            .map { SuggestionIdSource.Rated(sourceId = it.screenplayId, rating = it.personalRating) }

    private fun List<SuggestedScreenplay>.filterKnown(
        knownScreenplayIds: List<TmdbScreenplayId>
    ): Either<SuggestionError, NonEmptyList<SuggestedScreenplay>> {
        return filterNot { suggestedScreenplay -> suggestedScreenplay.screenplay.tmdbId in knownScreenplayIds }
            .nonEmpty { SuggestionError.NoSuggestions }
    }
}

class FakeGenerateSuggestions(
    private val result: Either<SuggestionError, NonEmptyList<SuggestedScreenplay>> =
        SuggestionError.NoSuggestions.left()
) : GenerateSuggestions {

    override operator fun invoke(
        type: ScreenplayType,
        suggestionsMode: SuggestionsMode
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedScreenplay>>> = flowOf(result)
}
