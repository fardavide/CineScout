package cinescout.anticipated.data.remote.model

import cinescout.screenplay.domain.model.id.TmdbMovieId
import cinescout.screenplay.domain.model.id.TraktScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody

typealias TraktMoviesAnticipatedMetadataResponse = List<TraktMovieAnticipatedMetadataBody>

@Serializable
data class TraktMovieAnticipatedMetadataBody(
    @SerialName(TraktContentType.Movie)
    val movie: TraktMovieMetadataBody
) : TraktScreenplayAnticipatedMetadataBody {

    override val tmdbId: TmdbMovieId
        get() = movie.ids.tmdb

    override val traktId: TraktScreenplayId
        get() = movie.ids.trakt
}
