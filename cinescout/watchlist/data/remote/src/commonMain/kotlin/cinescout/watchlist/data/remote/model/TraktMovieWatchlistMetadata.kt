package cinescout.watchlist.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody
import screenplay.data.remote.trakt.model.TraktScreenplayType

typealias TraktMoviesWatchlistMetadataResponse = List<TraktMovieWatchlistMetadataBody>

@Serializable
data class TraktMovieWatchlistMetadataBody(

    @SerialName(TraktScreenplayType.Movie)
    val movie: TraktMovieMetadataBody
)
