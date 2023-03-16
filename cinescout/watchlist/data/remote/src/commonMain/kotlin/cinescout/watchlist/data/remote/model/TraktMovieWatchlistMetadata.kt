package cinescout.watchlist.data.remote.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody
import screenplay.data.remote.trakt.model.TraktScreenplayType

typealias TraktMoviesWatchlistMetadataResponse = List<TraktMovieWatchlistMetadataBody>

@Serializable
@SerialName(TraktScreenplayType.Movie)
data class TraktMovieWatchlistMetadataBody(

    @SerialName(TraktScreenplayType.Movie)
    val movie: TraktMovieMetadataBody

) : TraktScreenplayWatchlistMetadataBody {

    override val tmdbId: TmdbScreenplayId.Movie
        get() = movie.ids.tmdb
}