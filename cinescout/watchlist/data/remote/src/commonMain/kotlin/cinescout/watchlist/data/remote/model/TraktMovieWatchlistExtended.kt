package cinescout.watchlist.data.remote.model

import cinescout.screenplay.domain.model.ids.TmdbMovieId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody

@Serializable
data class TraktMovieWatchlistExtendedBody(
    @SerialName(TraktContentType.Movie)
    val movie: TraktMovieExtendedBody
) : TraktScreenplayWatchlistExtendedBody {

    override val screenplay: TraktMovieExtendedBody
        get() = movie

    override val tmdbId: TmdbMovieId
        get() = movie.tmdbId
}
