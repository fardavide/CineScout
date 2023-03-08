package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktTvShowBody(

    @SerialName(TraktMovieBody.Ids)
    val ids: Ids,

    @SerialName(Title)
    val title: String
) {

    @Serializable
    data class Ids(

        @SerialName(Tmdb)
        val tmdb: TmdbScreenplayId.TvShow
    ) {

        companion object {

            const val Tmdb = "tmdb"
        }
    }

    companion object {

        const val Ids = "ids"
        const val Title = "title"
    }
}
