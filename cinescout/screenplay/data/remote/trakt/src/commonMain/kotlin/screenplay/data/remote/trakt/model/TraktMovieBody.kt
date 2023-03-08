package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktMovieBody(

    @SerialName(TraktMovieBody.Ids)
    val ids: Ids,

    @SerialName(Title)
    val title: String
) {

    @Serializable
    data class Ids(

        @SerialName(Tmdb)
        val tmdb: TmdbScreenplayId.Movie
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
