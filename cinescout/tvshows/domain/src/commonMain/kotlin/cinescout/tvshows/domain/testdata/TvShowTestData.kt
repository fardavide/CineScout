package cinescout.tvshows.domain.testdata

import arrow.core.some
import cinescout.common.model.PublicRating
import cinescout.common.model.Rating
import cinescout.common.model.TmdbBackdropImage
import cinescout.common.model.TmdbPosterImage
import cinescout.common.model.getOrThrow
import cinescout.tvshows.domain.model.TvShow
import com.soywiz.klock.Date

object TvShowTestData {

    val Grimm = TvShow(
        backdropImage = TmdbBackdropImage("oS3nip9GGsx5A7vWp8A1cazqJlF.jpg").some(),
        firstAirDate = Date(year = 2011, month = 10, day = 28),
        overview = "After Portland homicide detective Nick Burkhardt discovers he's descended from an elite " +
            "line of criminal profilers known as \"Grimms,\" he increasingly finds his responsibilities as a " +
            "detective at odds with his new responsibilities as a Grimm.",
        posterImage = TmdbPosterImage("iOptnt1QHi6bIHmOq6adnZTV0bU.jpg").some(),
        rating = PublicRating(voteCount = 2_613, average = Rating.of(8.259).getOrThrow()),
        title = "Grimm",
        tmdbId = TmdbTvShowIdTestData.Grimm
    )
}
