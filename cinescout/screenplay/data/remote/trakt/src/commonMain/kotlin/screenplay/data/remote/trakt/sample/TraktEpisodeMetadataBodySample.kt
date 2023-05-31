package screenplay.data.remote.trakt.sample

import cinescout.screenplay.domain.model.EpisodeNumber
import cinescout.screenplay.domain.model.SeasonNumber
import cinescout.screenplay.domain.sample.ScreenplayIdsSample
import screenplay.data.remote.trakt.model.TraktEpisodeIds
import screenplay.data.remote.trakt.model.TraktEpisodeMetadataBody

object TraktEpisodeMetadataBodySample {

    val BreakingBad_s1e1 = TraktEpisodeMetadataBody(
        number = EpisodeNumber(1),
        ids = TraktEpisodeIds(
            trakt = ScreenplayIdsSample.BreakingBad_s1e1.trakt,
            tmdb = ScreenplayIdsSample.BreakingBad_s1e1.tmdb
        ),
        season = SeasonNumber(1)
    )

    val BreakingBad_s1e2 = TraktEpisodeMetadataBody(
        number = EpisodeNumber(2),
        ids = TraktEpisodeIds(
            trakt = ScreenplayIdsSample.BreakingBad_s1e2.trakt,
            tmdb = ScreenplayIdsSample.BreakingBad_s1e2.tmdb
        ),
        season = SeasonNumber(1)
    )
}
