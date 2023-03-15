package cinescout.rating.data.remote.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody
import screenplay.data.remote.trakt.model.TraktScreenplayType

typealias TraktMoviesRatingsExtendedResponse = List<TraktMovieRatingExtendedBody>

@Serializable
data class TraktMovieRatingExtendedBody(

    @SerialName(TraktScreenplayType.Movie)
    val movie: TraktMovieExtendedBody,

    @SerialName(Rating)
    override val rating: Int

) : TraktScreenplayRatingExtendedBody {

    override val screenplay: TraktMovieExtendedBody
        get() = movie

    override val tmdbId: TmdbScreenplayId.Movie
        get() = movie.ids.tmdb
}
