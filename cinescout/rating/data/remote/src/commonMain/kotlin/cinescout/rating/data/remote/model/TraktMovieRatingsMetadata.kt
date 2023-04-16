package cinescout.rating.data.remote.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TraktScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody
import screenplay.data.remote.trakt.model.TraktScreenplayType

typealias TraktMoviesRatingsMetadataResponse = List<TraktMovieRatingMetadataBody>

@Serializable
@SerialName(TraktScreenplayType.Movie)
data class TraktMovieRatingMetadataBody(

    @SerialName(TraktScreenplayType.Movie)
    val movie: TraktMovieMetadataBody,

    @SerialName(Rating)
    override val rating: Int

) : TraktScreenplayRatingMetadataBody {

    override val tmdbId: TmdbScreenplayId
        get() = movie.ids.tmdb

    override val traktId: TraktScreenplayId
        get() = movie.ids.trakt
}
