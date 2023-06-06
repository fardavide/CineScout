package cinescout.recommended.data.remote.model

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
    @SerialName(TraktContentType.Movie)
    val movie: TraktMovieMetadataBody? = null,
    @SerialName(TraktContentType.TvShow)
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
