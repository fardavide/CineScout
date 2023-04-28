package cinescout.popular.data.remote.model

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

typealias TraktScreenplaysPopularMetadataResponse = List<TraktScreenplayPopularMetadataBody>

@Serializable(with = TraktScreenplayPopularMetadataSerializer::class)
sealed interface TraktScreenplayPopularMetadataBody {

    val tmdbId: TmdbScreenplayId

    val traktId: TraktScreenplayId

    val ids: ScreenplayIds
        get() = ScreenplayIds(tmdbId, traktId)
}

@Serializable
private data class TraktScreenplayPopularMetadataSurrogate(

    @SerialName(TraktScreenplayType.Movie)
    val movie: TraktMovieMetadataBody? = null,

    @SerialName(TraktScreenplayType.TvShow)
    val tvShow: TraktTvShowMetadataBody? = null
)

private object TraktScreenplayPopularMetadataSerializer : KSerializer<TraktScreenplayPopularMetadataBody> {

    override val descriptor = TraktScreenplayPopularMetadataSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): TraktScreenplayPopularMetadataBody {
        val surrogate = TraktScreenplayPopularMetadataSurrogate.serializer().deserialize(decoder)
        return when {
            surrogate.movie != null -> TraktMoviePopularMetadataBody(surrogate.movie)
            surrogate.tvShow != null -> TraktTvShowPopularMetadataBody(surrogate.tvShow)
            else -> error("Invalid Trakt screenplay watchlist metadata")
        }
    }

    override fun serialize(encoder: Encoder, value: TraktScreenplayPopularMetadataBody) {
        val surrogate = when (value) {
            is TraktMoviePopularMetadataBody -> TraktScreenplayPopularMetadataSurrogate(movie = value.movie)
            is TraktTvShowPopularMetadataBody -> TraktScreenplayPopularMetadataSurrogate(tvShow = value.tvShow)
        }
        TraktScreenplayPopularMetadataSurrogate.serializer().serialize(encoder, surrogate)
    }
}
