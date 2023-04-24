package cinescout.anticipated.data.remote.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TraktScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody
import screenplay.data.remote.trakt.model.TraktScreenplayType

typealias TraktMoviesAnticipatedMetadataResponse = List<TraktMovieAnticipatedMetadataBody>

@Serializable
data class TraktMovieAnticipatedMetadataBody(

    @SerialName(TraktScreenplayType.Movie)
    val movie: TraktMovieMetadataBody

) : TraktScreenplayAnticipatedMetadataBody {

    override val tmdbId: TmdbScreenplayId.Movie
        get() = movie.ids.tmdb

    override val traktId: TraktScreenplayId
        get() = movie.ids.trakt
}
