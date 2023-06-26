package cinescout.seasons.domain.sample

import arrow.core.some
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.SeasonNumber
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.screenplay.domain.model.id.SeasonIds
import cinescout.screenplay.domain.model.id.TmdbSeasonId
import cinescout.screenplay.domain.model.id.TraktSeasonId
import cinescout.seasons.domain.model.Season
import korlibs.time.Date

object SeasonSample {

    val BreakingBad_s0 = Season(
        firstAirDate = Date(year = 2009, month = 2, day = 18).some(),
        episodeCount = 11,
        ids = SeasonIds(
            tmdb = TmdbSeasonId(3577),
            trakt = TraktSeasonId(3949)
        ),
        number = SeasonNumber(0),
        rating = PublicRating(
            average = Rating.of(8.18785).getOrThrow(),
            voteCount = 181
        ),
        title = "Specials"
    )

    val BreakingBad_s1 = Season(
        firstAirDate = Date(year = 2008, month = 1, day = 21).some(),
        episodeCount = 7,
        ids = SeasonIds(
            tmdb = TmdbSeasonId(3572),
            trakt = TraktSeasonId(3950)
        ),
        number = SeasonNumber(1),
        rating = PublicRating(
            average = Rating.of(8.53699).getOrThrow(),
            voteCount = 2866
        ),
        title = "Season 1"
    )

    val BreakingBad_s2 = Season(
        firstAirDate = Date(year = 2009, month = 3, day = 9).some(),
        episodeCount = 13,
        ids = SeasonIds(
            tmdb = TmdbSeasonId(3573),
            trakt = TraktSeasonId(3951)
        ),
        number = SeasonNumber(2),
        rating = PublicRating(
            average = Rating.of(8.78454).getOrThrow(),
            voteCount = 2381
        ),
        title = "Season 2"
    )
}
