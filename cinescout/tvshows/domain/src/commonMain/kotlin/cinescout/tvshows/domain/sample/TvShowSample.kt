package cinescout.tvshows.domain.sample

import arrow.core.firstOrNone
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.testdata.TvShowImagesTestData
import com.soywiz.klock.Date

object TvShowSample {

    val BreakingBad = TvShow(
        backdropImage = TvShowImagesTestData.BreakingBad.backdrops.firstOrNone(),
        firstAirDate = Date(year = 2008, month = 1, day = 20),
        overview = "High school chemistry teacher Walter White's life is suddenly transformed by a dire " +
            "medical diagnosis. Street-savvy former student Jesse Pinkman teaches Walter a new trade.",
        posterImage = TvShowImagesTestData.BreakingBad.posters.firstOrNone(),
        rating = PublicRating(voteCount = 10_125, average = Rating.of(8.839).getOrThrow()),
        title = ScreenplaySample.BreakingBad.title,
        tmdbId = TmdbTvShowIdSample.BreakingBad
    )

    val Dexter = TvShow(
        backdropImage = TvShowImagesTestData.Dexter.backdrops.firstOrNone(),
        firstAirDate = Date(year = 2006, month = 10, day = 1),
        overview = "Dexter Morgan, a blood spatter pattern analyst for the Miami Metro Police also leads a " +
            "secret life as a serial killer, hunting down criminals who have slipped through the cracks of justice.",
        posterImage = TvShowImagesTestData.Dexter.posters.firstOrNone(),
        rating = PublicRating(voteCount = 3_233, average = Rating.of(8.191).getOrThrow()),
        title = ScreenplaySample.Dexter.title,
        tmdbId = TmdbTvShowIdSample.Dexter
    )

    val Grimm = TvShow(
        backdropImage = TvShowImagesTestData.Grimm.backdrops.firstOrNone(),
        firstAirDate = Date(year = 2011, month = 10, day = 28),
        overview = "After Portland homicide detective Nick Burkhardt discovers he's descended from an elite " +
            "line of criminal profilers known as Grimms, he increasingly finds his responsibilities as a " +
            "detective at odds with his new responsibilities as a Grimm.",
        posterImage = TvShowImagesTestData.Grimm.posters.firstOrNone(),
        rating = PublicRating(voteCount = 2_613, average = Rating.of(8.259).getOrThrow()),
        title = ScreenplaySample.Grimm.title,
        tmdbId = TmdbTvShowIdSample.Grimm
    )
}
