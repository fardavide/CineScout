package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktMovieIds(

    @SerialName(Tmdb)
    val tmdb: TmdbScreenplayId.Movie
) {

    companion object {

        const val Tmdb = "tmdb"
    }
}
