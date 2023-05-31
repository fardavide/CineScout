package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.ids.MovieIds
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias TraktMoviesMetadataResponse = List<TraktMovieMetadataBody>

@Serializable
@SerialName(TraktScreenplayType.Movie)
data class TraktMovieMetadataBody(
    @SerialName(TraktScreenplay.Ids)
    val ids: TraktMovieIds
) : TraktScreenplayMetadataBody {

    override fun ids(): MovieIds = MovieIds(
        trakt = ids.trakt,
        tmdb = ids.tmdb
    )
}

/**
 * Same as [TraktMovieMetadataBody] but with optional fields.
 */
@Serializable
data class OptTraktMovieMetadataBody(
    @SerialName(TraktScreenplay.Ids)
    val ids: OptTraktMovieIds
)
