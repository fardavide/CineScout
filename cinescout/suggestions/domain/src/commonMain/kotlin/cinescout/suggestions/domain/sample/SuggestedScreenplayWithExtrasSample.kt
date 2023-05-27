package cinescout.suggestions.domain.sample

import arrow.core.some
import cinescout.people.domain.sample.ScreenplayCreditsSample
import cinescout.rating.domain.sample.ScreenplayPersonalRatingSample
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.screenplay.domain.sample.ScreenplayGenresSample
import cinescout.screenplay.domain.sample.ScreenplayKeywordsSample
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.suggestions.domain.model.SuggestedMovieWithExtras
import cinescout.suggestions.domain.model.SuggestedTvShowWithExtras
import cinescout.watchlist.domain.sample.ScreenplayWatchlistSample

object SuggestedScreenplayWithExtrasSample {

    val BreakingBad = SuggestedTvShowWithExtras(
        affinity = SuggestedScreenplaySample.BreakingBad.affinity,
        credits = ScreenplayCreditsSample.BreakingBad,
        genres = ScreenplayGenresSample.BreakingBad,
        isInWatchlist = ScreenplayWatchlistSample.BreakingBad,
        keywords = ScreenplayKeywordsSample.BreakingBad,
        personalRating = ScreenplayPersonalRatingSample.BreakingBad.getOrThrow().some(),
        screenplay = ScreenplaySample.BreakingBad,
        source = SuggestedScreenplaySample.BreakingBad.source
    )

    val Dexter = SuggestedTvShowWithExtras(
        affinity = SuggestedScreenplaySample.Dexter.affinity,
        credits = ScreenplayCreditsSample.Dexter,
        genres = ScreenplayGenresSample.Dexter,
        isInWatchlist = ScreenplayWatchlistSample.Dexter,
        keywords = ScreenplayKeywordsSample.Dexter,
        personalRating = ScreenplayPersonalRatingSample.Dexter.getOrThrow().some(),
        screenplay = ScreenplaySample.Dexter,
        source = SuggestedScreenplaySample.Dexter.source
    )

    val Grimm = SuggestedTvShowWithExtras(
        affinity = SuggestedScreenplaySample.Grimm.affinity,
        credits = ScreenplayCreditsSample.Grimm,
        genres = ScreenplayGenresSample.Grimm,
        isInWatchlist = ScreenplayWatchlistSample.Grimm,
        keywords = ScreenplayKeywordsSample.Grimm,
        personalRating = ScreenplayPersonalRatingSample.Grimm.getOrThrow().some(),
        screenplay = ScreenplaySample.Grimm,
        source = SuggestedScreenplaySample.Grimm.source
    )

    val Inception = SuggestedMovieWithExtras(
        affinity = SuggestedScreenplaySample.Inception.affinity,
        credits = ScreenplayCreditsSample.Inception,
        genres = ScreenplayGenresSample.Inception,
        isInWatchlist = ScreenplayWatchlistSample.Inception,
        keywords = ScreenplayKeywordsSample.Inception,
        personalRating = ScreenplayPersonalRatingSample.Inception.getOrThrow().some(),
        screenplay = ScreenplaySample.Inception,
        source = SuggestedScreenplaySample.Inception.source
    )

    val TheWolfOfWallStreet = SuggestedMovieWithExtras(
        affinity = SuggestedScreenplaySample.TheWolfOfWallStreet.affinity,
        credits = ScreenplayCreditsSample.TheWolfOfWallStreet,
        genres = ScreenplayGenresSample.TheWolfOfWallStreet,
        isInWatchlist = ScreenplayWatchlistSample.TheWolfOfWallStreet,
        keywords = ScreenplayKeywordsSample.TheWolfOfWallStreet,
        personalRating = ScreenplayPersonalRatingSample.TheWolfOfWallStreet.getOrThrow().some(),
        screenplay = ScreenplaySample.TheWolfOfWallStreet,
        source = SuggestedScreenplaySample.TheWolfOfWallStreet.source
    )

    val War = SuggestedMovieWithExtras(
        affinity = SuggestedScreenplaySample.War.affinity,
        credits = ScreenplayCreditsSample.War,
        genres = ScreenplayGenresSample.War,
        isInWatchlist = ScreenplayWatchlistSample.War,
        keywords = ScreenplayKeywordsSample.War,
        personalRating = ScreenplayPersonalRatingSample.War.getOrThrow().some(),
        screenplay = ScreenplaySample.War,
        source = SuggestedScreenplaySample.War.source
    )
}
