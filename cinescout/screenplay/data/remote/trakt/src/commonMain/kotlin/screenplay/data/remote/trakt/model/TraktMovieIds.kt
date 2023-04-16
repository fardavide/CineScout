package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TraktScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktMovieIds(

    @SerialName(Tmdb)
    val tmdb: TmdbScreenplayId.Movie,

    @SerialName(Trakt)
    val trakt: TraktScreenplayId.Movie
) {

    val ids: ScreenplayIds.Movie
        get() = ScreenplayIds.Movie(tmdb, trakt)

    companion object {

        const val Tmdb = "tmdb"
        const val Trakt = "trakt"
    }
}

/**
 * Same as [TraktMovieIds] but with optional fields.
 */
@Serializable
data class OptTraktMovieIds(

    @SerialName(TraktMovieIds.Tmdb)
    val tmdb: TmdbScreenplayId.Movie? = null,

    @SerialName(TraktMovieIds.Trakt)
    val trakt: TraktScreenplayId.Movie? = null
)
