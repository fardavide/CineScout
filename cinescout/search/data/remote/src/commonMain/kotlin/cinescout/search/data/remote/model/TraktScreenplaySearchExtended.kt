package cinescout.search.data.remote.model

import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody
import screenplay.data.remote.trakt.model.TraktScreenplayExtendedBody
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowExtendedBody

typealias TraktScreenplaySearchExtendedResponse = List<TraktScreenplaySearchExtendedBody>

@Serializable(with = TraktScreenplaySearchExtendedSerializer::class)
sealed interface TraktScreenplaySearchExtendedBody {

    val screenplay: TraktScreenplayExtendedBody

    val tmdbId: TmdbScreenplayId
}

@Serializable
private data class TraktScreenplaySearchExtendedSurrogate(
    @SerialName(TraktScreenplayType.Movie)
    val movie: TraktMovieExtendedBody? = null,
    @SerialName(TraktScreenplayType.TvShow)
    val tvShow: TraktTvShowExtendedBody? = null
)

private object TraktScreenplaySearchExtendedSerializer : KSerializer<TraktScreenplaySearchExtendedBody> {

    override val descriptor = TraktScreenplaySearchExtendedSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): TraktScreenplaySearchExtendedBody {
        val surrogate = TraktScreenplaySearchExtendedSurrogate.serializer().deserialize(decoder)
        return when {
            surrogate.movie != null -> TraktMovieSearchExtendedBody(surrogate.movie)
            surrogate.tvShow != null -> TraktTvShowSearchExtendedBody(surrogate.tvShow)
            else -> error("Invalid Trakt screenplay search metadata")
        }
    }

    override fun serialize(encoder: Encoder, value: TraktScreenplaySearchExtendedBody) {
        val surrogate = when (value) {
            is TraktMovieSearchExtendedBody -> TraktScreenplaySearchExtendedSurrogate(movie = value.movie)
            is TraktTvShowSearchExtendedBody -> TraktScreenplaySearchExtendedSurrogate(tvShow = value.tvShow)
        }
        TraktScreenplaySearchExtendedSurrogate.serializer().serialize(encoder, surrogate)
    }
}
