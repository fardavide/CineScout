package cinescout.trending.data.remote.model

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

typealias TraktScreenplaysTrendingMetadataResponse = List<TraktScreenplayTrendingMetadataBody>

@Serializable(with = TraktScreenplayTrendingMetadataSerializer::class)
sealed interface TraktScreenplayTrendingMetadataBody {

    val tmdbId: TmdbScreenplayId

    val traktId: TraktScreenplayId

    val ids: ScreenplayIds
        get() = ScreenplayIds(tmdbId, traktId)
}

@Serializable
private data class TraktScreenplayTrendingMetadataSurrogate(
    @SerialName(TraktScreenplayType.Movie)
    val movie: TraktMovieMetadataBody? = null,
    @SerialName(TraktScreenplayType.TvShow)
    val tvShow: TraktTvShowMetadataBody? = null
)

private object TraktScreenplayTrendingMetadataSerializer : KSerializer<TraktScreenplayTrendingMetadataBody> {

    override val descriptor = TraktScreenplayTrendingMetadataSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): TraktScreenplayTrendingMetadataBody {
        val surrogate = TraktScreenplayTrendingMetadataSurrogate.serializer().deserialize(decoder)
        return when {
            surrogate.movie != null -> TraktMovieTrendingMetadataBody(surrogate.movie)
            surrogate.tvShow != null -> TraktTvShowTrendingMetadataBody(surrogate.tvShow)
            else -> error("Invalid Trakt screenplay watchlist metadata")
        }
    }

    override fun serialize(encoder: Encoder, value: TraktScreenplayTrendingMetadataBody) {
        val surrogate = when (value) {
            is TraktMovieTrendingMetadataBody -> TraktScreenplayTrendingMetadataSurrogate(movie = value.movie)
            is TraktTvShowTrendingMetadataBody -> TraktScreenplayTrendingMetadataSurrogate(tvShow = value.tvShow)
        }
        TraktScreenplayTrendingMetadataSurrogate.serializer().serialize(encoder, surrogate)
    }
}
