package cinescout.recommended.data.remote.model

import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TraktScreenplayId
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

typealias TraktScreenplaysRecommendedMetadataResponse = List<TraktScreenplayRecommendedMetadataBody>

@Serializable(with = TraktScreenplayRecommendedMetadataSerializer::class)
sealed interface TraktScreenplayRecommendedMetadataBody {

    val tmdbId: TmdbScreenplayId

    val traktId: TraktScreenplayId

    val ids: ScreenplayIds
        get() = ScreenplayIds(tmdbId, traktId)
}

@Serializable
private data class TraktScreenplayRecommendedMetadataSurrogate(

    @SerialName(TraktScreenplayType.Movie)
    val movie: TraktMovieMetadataBody? = null,

    @SerialName(TraktScreenplayType.TvShow)
    val tvShow: TraktTvShowMetadataBody? = null
)

private object TraktScreenplayRecommendedMetadataSerializer : KSerializer<TraktScreenplayRecommendedMetadataBody> {

    override val descriptor = TraktScreenplayRecommendedMetadataSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): TraktScreenplayRecommendedMetadataBody {
        val surrogate = TraktScreenplayRecommendedMetadataSurrogate.serializer().deserialize(decoder)
        return when {
            surrogate.movie != null -> TraktMovieRecommendedMetadataBody(surrogate.movie)
            surrogate.tvShow != null -> TraktTvShowRecommendedMetadataBody(surrogate.tvShow)
            else -> error("Invalid Trakt screenplay watchlist metadata")
        }
    }

    override fun serialize(encoder: Encoder, value: TraktScreenplayRecommendedMetadataBody) {
        val surrogate = when (value) {
            is TraktMovieRecommendedMetadataBody -> TraktScreenplayRecommendedMetadataSurrogate(movie = value.movie)
            is TraktTvShowRecommendedMetadataBody -> TraktScreenplayRecommendedMetadataSurrogate(tvShow = value.tvShow)
        }
        TraktScreenplayRecommendedMetadataSurrogate.serializer().serialize(encoder, surrogate)
    }
}
