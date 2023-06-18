package cinescout.rating.data.remote.model

import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.model.ids.TraktScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody

typealias TraktMoviesRatingsMetadataResponse = List<TraktMovieRatingMetadataBody>

@Serializable
@SerialName(TraktContentType.Movie)
data class TraktMovieRatingMetadataBody(

    @SerialName(TraktContentType.Movie)
    val movie: TraktMovieMetadataBody,

    @SerialName(Rating)
    override val rating: Int

) : TraktScreenplayRatingMetadataBody {

    override val tmdbId: TmdbScreenplayId
        get() = movie.ids.tmdb

    override val traktId: TraktScreenplayId
        get() = movie.ids.trakt
}
