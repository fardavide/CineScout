package cinescout.watchlist.data.remote.model

import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.model.ids.TraktScreenplayId
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

typealias TraktScreenplaysWatchlistMetadataResponse = List<TraktScreenplayWatchlistMetadataBody>

@Serializable(with = TraktScreenplayWatchlistMetadataSerializer::class)
sealed interface TraktScreenplayWatchlistMetadataBody {

    val tmdbId: TmdbScreenplayId

    val traktId: TraktScreenplayId

    val ids: ScreenplayIds
        get() = ScreenplayIds(tmdbId, traktId)
}

@Serializable
private data class TraktScreenplayWatchlistMetadataSurrogate(
    @SerialName(TraktContentType.Movie)
    val movie: TraktMovieMetadataBody? = null,
    @SerialName(TraktContentType.TvShow)
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
