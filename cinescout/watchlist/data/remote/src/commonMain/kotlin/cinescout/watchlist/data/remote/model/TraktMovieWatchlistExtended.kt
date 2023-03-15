package cinescout.watchlist.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody
import screenplay.data.remote.trakt.model.TraktScreenplayType

typealias TraktMoviesWatchlistExtendedResponse = List<TraktMovieWatchlistExtendedBody>

@Serializable
@SerialName(TraktScreenplayType.Movie)
data class TraktMovieWatchlistExtendedBody(

    @SerialName(TraktScreenplayType.Movie)
    val movie: TraktMovieExtendedBody

) : TraktScreenplayWatchlistExtendedBody {

    override val screenplay: TraktMovieExtendedBody
        get() = movie
}
