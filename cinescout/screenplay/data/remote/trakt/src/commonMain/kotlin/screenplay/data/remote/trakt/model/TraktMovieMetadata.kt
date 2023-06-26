package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.id.MovieIds
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias TraktMoviesMetadataResponse = List<TraktMovieMetadataBody>

@Serializable
@SerialName(TraktContentType.Movie)
data class TraktMovieMetadataBody(
    @SerialName(TraktContent.Ids)
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
    @SerialName(TraktContent.Ids)
    val ids: OptTraktMovieIds
)
