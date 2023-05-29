package cinescout.suggestions.domain.sample

import arrow.core.some
import cinescout.history.domain.sample.ScreenplayHistorySample
import cinescout.media.domain.sample.ScreenplayMediaSample
import cinescout.people.domain.sample.ScreenplayCreditsSample
import cinescout.rating.domain.sample.ScreenplayPersonalRatingSample
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.screenplay.domain.sample.ScreenplayGenresSample
import cinescout.screenplay.domain.sample.ScreenplayKeywordsSample
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.suggestions.domain.model.SuggestedScreenplayWithExtra
import cinescout.watchlist.domain.sample.ScreenplayWatchlistSample

object SuggestedScreenplayWithExtraSample {

    val BreakingBad = SuggestedScreenplayWithExtra(
        affinity = SuggestedScreenplaySample.BreakingBad.affinity,
        credits = ScreenplayCreditsSample.BreakingBad,
        genres = ScreenplayGenresSample.BreakingBad,
        history = ScreenplayHistorySample.BreakingBad,
        isInWatchlist = ScreenplayWatchlistSample.BreakingBad,
        keywords = ScreenplayKeywordsSample.BreakingBad,
        media = ScreenplayMediaSample.BreakingBad,
        personalRating = ScreenplayPersonalRatingSample.BreakingBad.getOrThrow().some(),
        screenplay = ScreenplaySample.BreakingBad,
        source = SuggestedScreenplaySample.BreakingBad.source
    )

    val Dexter = SuggestedScreenplayWithExtra(
        affinity = SuggestedScreenplaySample.Dexter.affinity,
        credits = ScreenplayCreditsSample.Dexter,
        genres = ScreenplayGenresSample.Dexter,
        history = ScreenplayHistorySample.Dexter,
        isInWatchlist = ScreenplayWatchlistSample.Dexter,
        keywords = ScreenplayKeywordsSample.Dexter,
        media = ScreenplayMediaSample.Dexter,
        personalRating = ScreenplayPersonalRatingSample.Dexter.getOrThrow().some(),
        screenplay = ScreenplaySample.Dexter,
        source = SuggestedScreenplaySample.Dexter.source
    )

    val Grimm = SuggestedScreenplayWithExtra(
        affinity = SuggestedScreenplaySample.Grimm.affinity,
        credits = ScreenplayCreditsSample.Grimm,
        genres = ScreenplayGenresSample.Grimm,
        history = ScreenplayHistorySample.Grimm,
        isInWatchlist = ScreenplayWatchlistSample.Grimm,
        keywords = ScreenplayKeywordsSample.Grimm,
        media = ScreenplayMediaSample.Grimm,
        personalRating = ScreenplayPersonalRatingSample.Grimm.getOrThrow().some(),
        screenplay = ScreenplaySample.Grimm,
        source = SuggestedScreenplaySample.Grimm.source
    )

    val Inception = SuggestedScreenplayWithExtra(
        affinity = SuggestedScreenplaySample.Inception.affinity,
        credits = ScreenplayCreditsSample.Inception,
        genres = ScreenplayGenresSample.Inception,
        history = ScreenplayHistorySample.Inception,
        isInWatchlist = ScreenplayWatchlistSample.Inception,
        keywords = ScreenplayKeywordsSample.Inception,
        media = ScreenplayMediaSample.Inception,
        personalRating = ScreenplayPersonalRatingSample.Inception.getOrThrow().some(),
        screenplay = ScreenplaySample.Inception,
        source = SuggestedScreenplaySample.Inception.source
    )

    val TheWolfOfWallStreet = SuggestedScreenplayWithExtra(
        affinity = SuggestedScreenplaySample.TheWolfOfWallStreet.affinity,
        credits = ScreenplayCreditsSample.TheWolfOfWallStreet,
        genres = ScreenplayGenresSample.TheWolfOfWallStreet,
        history = ScreenplayHistorySample.TheWolfOfWallStreet,
        isInWatchlist = ScreenplayWatchlistSample.TheWolfOfWallStreet,
        keywords = ScreenplayKeywordsSample.TheWolfOfWallStreet,
        media = ScreenplayMediaSample.TheWolfOfWallStreet,
        personalRating = ScreenplayPersonalRatingSample.TheWolfOfWallStreet.getOrThrow().some(),
        screenplay = ScreenplaySample.TheWolfOfWallStreet,
        source = SuggestedScreenplaySample.TheWolfOfWallStreet.source
    )

    val War = SuggestedScreenplayWithExtra(
        affinity = SuggestedScreenplaySample.War.affinity,
        credits = ScreenplayCreditsSample.War,
        genres = ScreenplayGenresSample.War,
        history = ScreenplayHistorySample.War,
        isInWatchlist = ScreenplayWatchlistSample.War,
        keywords = ScreenplayKeywordsSample.War,
        media = ScreenplayMediaSample.War,
        personalRating = ScreenplayPersonalRatingSample.War.getOrThrow().some(),
        screenplay = ScreenplaySample.War,
        source = SuggestedScreenplaySample.War.source
    )
}
