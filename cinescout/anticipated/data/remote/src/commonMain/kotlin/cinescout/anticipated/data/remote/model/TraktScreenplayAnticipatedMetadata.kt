package cinescout.anticipated.data.remote.model

import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.model.ids.TraktScreenplayId
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

typealias TraktScreenplaysAnticipatedMetadataResponse = List<TraktScreenplayAnticipatedMetadataBody>

@Serializable(with = TraktScreenplayAnticipatedMetadataSerializer::class)
sealed interface TraktScreenplayAnticipatedMetadataBody {

    val tmdbId: TmdbScreenplayId

    val traktId: TraktScreenplayId

    val ids: ScreenplayIds
        get() = ScreenplayIds(tmdbId, traktId)
}

@Serializable
private data class TraktScreenplayAnticipatedMetadataSurrogate(
    @SerialName(TraktScreenplayType.Movie)
    val movie: TraktMovieMetadataBody? = null,
    @SerialName(TraktScreenplayType.TvShow)
    val tvShow: TraktTvShowMetadataBody? = null
)

private object TraktScreenplayAnticipatedMetadataSerializer : KSerializer<TraktScreenplayAnticipatedMetadataBody> {

    override val descriptor = TraktScreenplayAnticipatedMetadataSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): TraktScreenplayAnticipatedMetadataBody {
        val surrogate = TraktScreenplayAnticipatedMetadataSurrogate.serializer().deserialize(decoder)
        return when {
            surrogate.movie != null -> TraktMovieAnticipatedMetadataBody(surrogate.movie)
            surrogate.tvShow != null -> TraktTvShowAnticipatedMetadataBody(surrogate.tvShow)
            else -> error("Invalid Trakt screenplay watchlist metadata")
        }
    }

    override fun serialize(encoder: Encoder, value: TraktScreenplayAnticipatedMetadataBody) {
        val surrogate = when (value) {
            is TraktMovieAnticipatedMetadataBody -> TraktScreenplayAnticipatedMetadataSurrogate(movie = value.movie)
            is TraktTvShowAnticipatedMetadataBody -> TraktScreenplayAnticipatedMetadataSurrogate(tvShow = value.tvShow)
        }
        TraktScreenplayAnticipatedMetadataSurrogate.serializer().serialize(encoder, surrogate)
    }
}
