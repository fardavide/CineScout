package cinescout.rating.data.remote.model

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

typealias TraktScreenplaysRatingsMetadataResponse = List<TraktScreenplayRatingMetadataBody>

@Serializable(with = TraktScreenplayRatingMetadataSerializer::class)
sealed interface TraktScreenplayRatingMetadataBody {

    val tmdbId: TmdbScreenplayId

    val traktId: TraktScreenplayId

    @SerialName(Rating)
    val rating: Int

    val ids: ScreenplayIds
        get() = ScreenplayIds(tmdbId, traktId)
}

@Serializable
private data class TraktScreenplayRatingMetadataSurrogate(

    @SerialName(TraktContentType.Movie)
    val movie: TraktMovieMetadataBody? = null,

    @SerialName(TraktContentType.TvShow)
    val tvShow: TraktTvShowMetadataBody? = null,

    @SerialName(Rating)
    val rating: Int
)

private object TraktScreenplayRatingMetadataSerializer : KSerializer<TraktScreenplayRatingMetadataBody> {

    override val descriptor = TraktScreenplayRatingMetadataSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): TraktScreenplayRatingMetadataBody {
        val surrogate = TraktScreenplayRatingMetadataSurrogate.serializer().deserialize(decoder)
        return when {
            surrogate.movie != null ->
                TraktMovieRatingMetadataBody(movie = surrogate.movie, rating = surrogate.rating)
            surrogate.tvShow != null ->
                TraktTvShowRatingMetadataBody(tvShow = surrogate.tvShow, rating = surrogate.rating)
            else -> error("Invalid Trakt screenplay rating metadata")
        }
    }

    override fun serialize(encoder: Encoder, value: TraktScreenplayRatingMetadataBody) {
        val surrogate = when (value) {
            is TraktMovieRatingMetadataBody ->
                TraktScreenplayRatingMetadataSurrogate(movie = value.movie, rating = value.rating)
            is TraktTvShowRatingMetadataBody ->
                TraktScreenplayRatingMetadataSurrogate(tvShow = value.tvShow, rating = value.rating)
        }
        TraktScreenplayRatingMetadataSurrogate.serializer().serialize(encoder, surrogate)
    }
}
