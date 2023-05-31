package cinescout.watchlist.data.remote.model

import cinescout.screenplay.domain.model.ids.TmdbMovieId
import cinescout.screenplay.domain.model.ids.TraktScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody
import screenplay.data.remote.trakt.model.TraktScreenplayType

typealias TraktMoviesWatchlistMetadataResponse = List<TraktMovieWatchlistMetadataBody>

@Serializable
data class TraktMovieWatchlistMetadataBody(
    @SerialName(TraktScreenplayType.Movie)
    val movie: TraktMovieMetadataBody
) : TraktScreenplayWatchlistMetadataBody {

    override val tmdbId: TmdbMovieId
        get() = movie.ids.tmdb

    override val traktId: TraktScreenplayId
        get() = movie.ids.trakt
}
