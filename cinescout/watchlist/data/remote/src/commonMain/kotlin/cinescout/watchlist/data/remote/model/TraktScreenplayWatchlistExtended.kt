package cinescout.watchlist.data.remote.model

import cinescout.screenplay.domain.model.id.TmdbScreenplayId
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody
import screenplay.data.remote.trakt.model.TraktScreenplayExtendedBody
import screenplay.data.remote.trakt.model.TraktTvShowExtendedBody

typealias TraktScreenplaysWatchlistExtendedResponse = List<TraktScreenplayWatchlistExtendedBody>

@Serializable(with = TraktScreenplayWatchlistExtendedSerializer::class)
sealed interface TraktScreenplayWatchlistExtendedBody {

    val screenplay: TraktScreenplayExtendedBody

    val tmdbId: TmdbScreenplayId
}

@Serializable
private data class TraktScreenplayWatchlistExtendedSurrogate(
    @SerialName(TraktContentType.Movie)
    val movie: TraktMovieExtendedBody? = null,
    @SerialName(TraktContentType.TvShow)
    val tvShow: TraktTvShowExtendedBody? = null
)

private object TraktScreenplayWatchlistExtendedSerializer : KSerializer<TraktScreenplayWatchlistExtendedBody> {

    override val descriptor = TraktScreenplayWatchlistExtendedSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): TraktScreenplayWatchlistExtendedBody {
        val surrogate = TraktScreenplayWatchlistExtendedSurrogate.serializer().deserialize(decoder)
        return when {
            surrogate.movie != null -> TraktMovieWatchlistExtendedBody(surrogate.movie)
            surrogate.tvShow != null -> TraktTvShowWatchlistExtendedBody(surrogate.tvShow)
            else -> error("Invalid Trakt screenplay watchlist metadata")
        }
    }

    override fun serialize(encoder: Encoder, value: TraktScreenplayWatchlistExtendedBody) {
        val surrogate = when (value) {
            is TraktMovieWatchlistExtendedBody -> TraktScreenplayWatchlistExtendedSurrogate(movie = value.movie)
            is TraktTvShowWatchlistExtendedBody -> TraktScreenplayWatchlistExtendedSurrogate(tvShow = value.tvShow)
        }
        TraktScreenplayWatchlistExtendedSurrogate.serializer().serialize(encoder, surrogate)
    }
}

