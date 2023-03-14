package cinescout.watchlist.data.remote.res

import cinescout.screenplay.domain.sample.ScreenplaySample
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowIds
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

object TraktTvShowWatchlistMetadataJson {

    val OneTvShow = """
        [
            {
                "${TraktScreenplayType.TvShow}": {
                    "${TraktTvShowMetadataBody.Title}": "${ScreenplaySample.Grimm.title}",
                    "${TraktTvShowMetadataBody.Ids}": {
                        "${TraktTvShowIds.Tmdb}": ${ScreenplaySample.Grimm.tmdbId.value}
                    }
                }
            }
        ]
    """.trimIndent()
}
