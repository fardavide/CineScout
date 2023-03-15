package cinescout.watchlist.data.remote.res

import cinescout.screenplay.domain.sample.ScreenplaySample
import screenplay.data.remote.trakt.model.TraktMovieIds
import screenplay.data.remote.trakt.model.TraktScreenplay
import screenplay.data.remote.trakt.model.TraktScreenplayType

object TraktMovieWatchlistMetadataJson {

    val OneMovie = """
        [
            {
                "${TraktScreenplayType.Movie}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktMovieIds.Tmdb}": ${ScreenplaySample.Grimm.tmdbId.value}
                    }
                }
            }
        ]
    """.trimIndent()
}
