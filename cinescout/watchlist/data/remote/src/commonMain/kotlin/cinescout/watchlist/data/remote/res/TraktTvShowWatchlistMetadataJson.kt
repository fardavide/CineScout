package cinescout.watchlist.data.remote.res

import cinescout.screenplay.domain.sample.ScreenplaySample
import screenplay.data.remote.trakt.model.TraktScreenplay
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowIds

object TraktTvShowWatchlistMetadataJson {

    val OneTvShow = """
        [
            {
                "${TraktScreenplayType.TvShow}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktTvShowIds.Tmdb}": ${ScreenplaySample.Grimm.tmdbId.value}
                    }
                }
            }
        ]
    """.trimIndent()
}
