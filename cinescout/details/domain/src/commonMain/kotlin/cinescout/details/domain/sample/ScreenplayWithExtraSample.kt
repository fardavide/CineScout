package cinescout.details.domain.sample

import cinescout.details.domain.model.ScreenplayWithExtra
import cinescout.history.domain.sample.HistorySample
import cinescout.media.domain.sample.ScreenplayMediaSample
import cinescout.people.domain.sample.ScreenplayCreditsSample
import cinescout.rating.domain.sample.ScreenplayPersonalRatingSample
import cinescout.screenplay.domain.sample.ScreenplayGenresSample
import cinescout.screenplay.domain.sample.ScreenplayKeywordsSample
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.watchlist.domain.sample.ScreenplayWatchlistSample

object ScreenplayWithExtraSample {

    val Inception = ScreenplayWithExtra(
        screenplay = ScreenplaySample.Inception,
        credits = ScreenplayCreditsSample.Inception,
        genres = ScreenplayGenresSample.Inception,
        history = HistorySample.Inception,
        isInWatchlist = ScreenplayWatchlistSample.Inception,
        keywords = ScreenplayKeywordsSample.Inception,
        media = ScreenplayMediaSample.Inception,
        personalRating = ScreenplayPersonalRatingSample.Inception.toOption()
    )
}
