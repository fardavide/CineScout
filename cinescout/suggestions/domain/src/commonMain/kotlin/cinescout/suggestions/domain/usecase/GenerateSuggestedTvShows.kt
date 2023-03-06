package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.flatMap
import arrow.core.getOrElse
import arrow.core.left
import cinescout.error.DataError
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionSource
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowWithPersonalRating
import cinescout.tvshows.domain.usecase.GetAllDislikedTvShows
import cinescout.tvshows.domain.usecase.GetAllLikedTvShows
import cinescout.tvshows.domain.usecase.GetAllRatedTvShows
import cinescout.tvshows.domain.usecase.GetAllWatchlistTvShows
import cinescout.utils.kotlin.combineLatest
import cinescout.utils.kotlin.nonEmpty
import cinescout.utils.kotlin.randomOrNone
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import store.PagedData
import store.Paging
import store.Refresh

@Factory
class GenerateSuggestedTvShows(
    private val getAllDislikedTvShows: GetAllDislikedTvShows,
    private val getAllLikedTvShows: GetAllLikedTvShows,
    private val getAllRatedTvShows: GetAllRatedTvShows,
    private val getAllWatchlistTvShows: GetAllWatchlistTvShows,
    private val tvShowRepository: TvShowRepository
) {

    operator fun invoke(
        suggestionsMode: SuggestionsMode
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedTvShow>>> = combineLatest(
        getAllDislikedTvShows(),
        getAllLikedTvShows(),
        getAllRatedTvShows(refresh = Refresh.IfNeeded),
        getAllWatchlistTvShows(refresh = Refresh.IfNeeded)
    ) { disliked, liked, ratedEither, watchlistEither ->

        val rated = ratedEither
            .getOrElse { emptyList() }

        val watchlist = watchlistEither
            .getOrElse { emptyList() }

        val positiveTvShows = liked.withLikedSource() + rated.withRatedSource() + watchlist.withWatchlistSource()
        val (tvShowId, source) = positiveTvShows.randomOrNone().map { it.tvShow.tmdbId to it.source }
            .getOrElse { return@combineLatest flowOf(SuggestionError.NoSuggestions.left()) }

        getRecommendationsFor(tvShowId, suggestionsMode).map { dataEither ->
            dataEither.mapLeft {
                SuggestionError.Source(it as DataError.Remote)
            }.flatMap { pagedData ->
                val suggestionsPagedData = pagedData.map { SuggestedTvShow(it, source) }
                val allKnownTvShows = disliked + liked + rated.map { it.tvShow } + watchlist
                suggestionsPagedData.data.filterKnownTvShows(allKnownTvShows)
            }
        }
    }

    private fun getRecommendationsFor(
        movieId: TmdbTvShowId,
        suggestionsMode: SuggestionsMode
    ): Flow<Either<DataError, PagedData<TvShow, Paging>>> = when (suggestionsMode) {
        SuggestionsMode.Deep ->
            tvShowRepository.getRecommendationsFor(movieId, refresh = Refresh.Once).filterIntermediatePages()
        SuggestionsMode.Quick ->
            tvShowRepository.getRecommendationsFor(movieId, refresh = Refresh.IfNeeded)
    }

    private fun List<TvShow>.withLikedSource(): List<SuggestedTvShow> =
        map { SuggestedTvShow(it, SuggestionSource.FromLiked(it.title)) }

    private fun List<TvShow>.withWatchlistSource(): List<SuggestedTvShow> =
        map { SuggestedTvShow(it, SuggestionSource.FromWatchlist(it.title)) }

    private fun List<TvShowWithPersonalRating>.withRatedSource(): List<SuggestedTvShow> =
        filter { it.personalRating.value >= 6 }
            .map { SuggestedTvShow(it.tvShow, SuggestionSource.FromRated(it.tvShow.title, it.personalRating)) }

    private fun List<SuggestedTvShow>.filterKnownTvShows(
        knownTvShows: List<TvShow>
    ): Either<SuggestionError, NonEmptyList<SuggestedTvShow>> {
        val knownTvShowIds = knownTvShows.map { it.tmdbId }
        return filterNot { suggestedTvShow -> suggestedTvShow.tvShow.tmdbId in knownTvShowIds }
            .nonEmpty { SuggestionError.NoSuggestions }
    }
}
