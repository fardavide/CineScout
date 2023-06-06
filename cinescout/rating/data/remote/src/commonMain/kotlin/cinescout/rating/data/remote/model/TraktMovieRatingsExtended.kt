package cinescout.rating.data.remote.model

import cinescout.screenplay.domain.model.ids.TmdbMovieId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody

typealias TraktMoviesRatingsExtendedResponse = List<TraktMovieRatingExtendedBody>

@Serializable
@SerialName(TraktContentType.Movie)
data class TraktMovieRatingExtendedBody(
    @SerialName(TraktContentType.Movie)
    val movie: TraktMovieExtendedBody,
    @SerialName(Rating)
    override val rating: Int
) : TraktScreenplayRatingExtendedBody {

    override val screenplay: TraktMovieExtendedBody
        get() = movie

    override val tmdbId: TmdbMovieId
        get() = movie.tmdbId
}
