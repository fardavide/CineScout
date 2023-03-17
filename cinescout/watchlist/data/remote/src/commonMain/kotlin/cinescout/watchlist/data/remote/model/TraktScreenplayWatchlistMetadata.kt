package cinescout.watchlist.data.remote.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

typealias TraktScreenplaysWatchlistMetadataResponse = List<TraktScreenplayWatchlistMetadataBody>

@Serializable(with = TraktScreenplayWatchlistMetadataSerializer::class)
sealed interface TraktScreenplayWatchlistMetadataBody {

    val tmdbId: TmdbScreenplayId
}

@Serializable
private data class TraktScreenplayWatchlistMetadataSurrogate(

    @SerialName(TraktScreenplayType.Movie)
    val movie: TraktMovieMetadataBody? = null,

    @SerialName(TraktScreenplayType.TvShow)
    val tvShow: TraktTvShowMetadataBody? = null
)

private object TraktScreenplayWatchlistMetadataSerializer : KSerializer<TraktScreenplayWatchlistMetadataBody> {

    override val descriptor = TraktScreenplayWatchlistMetadataSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): TraktScreenplayWatchlistMetadataBody {
        val surrogate = TraktScreenplayWatchlistMetadataSurrogate.serializer().deserialize(decoder)
        return when {
            surrogate.movie != null -> TraktMovieWatchlistMetadataBody(surrogate.movie)
            surrogate.tvShow != null -> TraktTvShowWatchlistMetadataBody(surrogate.tvShow)
            else -> error("Invalid Trakt screenplay watchlist metadata")
        }
    }

    override fun serialize(encoder: Encoder, value: TraktScreenplayWatchlistMetadataBody) {
        val surrogate = when (value) {
            is TraktMovieWatchlistMetadataBody -> TraktScreenplayWatchlistMetadataSurrogate(movie = value.movie)
            is TraktTvShowWatchlistMetadataBody -> TraktScreenplayWatchlistMetadataSurrogate(tvShow = value.tvShow)
        }
        TraktScreenplayWatchlistMetadataSurrogate.serializer().serialize(encoder, surrogate)
    }
}
