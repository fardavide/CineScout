package cinescout.watchlist.data.remote.res

import cinescout.screenplay.domain.sample.ScreenplaySample
import screenplay.data.remote.trakt.model.TraktMovieIds
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody
import screenplay.data.remote.trakt.model.TraktScreenplayType

object TraktMovieWatchlistMetadataJson {

    val OneMovie = """
        [
            {
                "${TraktScreenplayType.Movie}": {
                    "${TraktMovieMetadataBody.Title}": "${ScreenplaySample.Grimm.title}",
                    "${TraktMovieMetadataBody.Ids}": {
                        "${TraktMovieIds.Tmdb}": ${ScreenplaySample.Grimm.tmdbId.value}
                    }
                }
            }
        ]
    """.trimIndent()
}
