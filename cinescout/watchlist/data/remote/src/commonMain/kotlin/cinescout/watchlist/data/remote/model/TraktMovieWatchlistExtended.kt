package cinescout.watchlist.data.remote.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody
import screenplay.data.remote.trakt.model.TraktScreenplayType

typealias TraktMoviesWatchlistExtendedResponse = List<TraktMovieWatchlistExtendedBody>

@Serializable
data class TraktMovieWatchlistExtendedBody(

    @SerialName(TraktScreenplayType.Movie)
    val movie: TraktMovieExtendedBody

) : TraktScreenplayWatchlistExtendedBody {

    override val screenplay: TraktMovieExtendedBody
        get() = movie

    override val tmdbId: TmdbScreenplayId.Movie
        get() = movie.tmdbId
}
