package cinescout.rating.data.remote.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody
import screenplay.data.remote.trakt.model.TraktScreenplayExtendedBody
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowExtendedBody

typealias TraktScreenplaysRatingsExtendedResponse = List<TraktScreenplayRatingExtendedBody>

@Serializable(with = TraktScreenplayRatingExtendedSerializer::class)
sealed interface TraktScreenplayRatingExtendedBody {

    val screenplay: TraktScreenplayExtendedBody

    val tmdbId: TmdbScreenplayId

    @SerialName(Rating)
    val rating: Int
}

@Serializable
private data class TraktScreenplayRatingExtendedSurrogate(

    @SerialName(TraktScreenplayType.Movie)
    val movie: TraktMovieExtendedBody? = null,

    @SerialName(TraktScreenplayType.TvShow)
    val tvShow: TraktTvShowExtendedBody? = null,

    @SerialName(Rating)
    val rating: Int
)

private object TraktScreenplayRatingExtendedSerializer : KSerializer<TraktScreenplayRatingExtendedBody> {

    override val descriptor = TraktScreenplayRatingExtendedSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): TraktScreenplayRatingExtendedBody {
        val surrogate = TraktScreenplayRatingExtendedSurrogate.serializer().deserialize(decoder)
        return when {
            surrogate.movie != null -> TraktMovieRatingExtendedBody(surrogate.movie, surrogate.rating)
            surrogate.tvShow != null -> TraktTvShowRatingExtendedBody(surrogate.tvShow, surrogate.rating)
            else -> error("Invalid Trakt screenplay rating metadata")
        }
    }

    override fun serialize(encoder: Encoder, value: TraktScreenplayRatingExtendedBody) {
        val surrogate = when (value) {
            is TraktMovieRatingExtendedBody ->
                TraktScreenplayRatingExtendedSurrogate(movie = value.movie, rating = value.rating)
            is TraktTvShowRatingExtendedBody ->
                TraktScreenplayRatingExtendedSurrogate(tvShow = value.tvShow, rating = value.rating)
        }
        TraktScreenplayRatingExtendedSurrogate.serializer().serialize(encoder, surrogate)
    }
}
