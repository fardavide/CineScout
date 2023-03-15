package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias TraktMoviesMetadataResponse = List<TraktMovieMetadataBody>

@Serializable
data class TraktMovieMetadataBody(

    @SerialName(TraktScreenplay.Ids)
    val ids: TraktMovieIds

) : TraktScreenplayMetadataBody {

    override val tmdbId: TmdbScreenplayId.Movie
        get() = ids.tmdb
}
