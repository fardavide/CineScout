package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktTvShowIds(

    @SerialName(Tmdb)
    val tmdb: TmdbScreenplayId.TvShow
) {

    companion object {

        const val Tmdb = "tmdb"
    }
}
