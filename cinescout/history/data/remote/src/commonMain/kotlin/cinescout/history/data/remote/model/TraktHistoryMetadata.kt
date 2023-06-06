package cinescout.history.data.remote.model

import cinescout.history.domain.model.HistoryItemId
import cinescout.screenplay.domain.model.ids.ContentIds
import cinescout.screenplay.domain.model.ids.TmdbContentId
import cinescout.screenplay.domain.model.ids.TraktContentId
import korlibs.time.DateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktEpisodeMetadataBody
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

typealias TraktHistoryMetadataResponse = List<TraktHistoryMetadataBody>

@Serializable(with = TraktHistoryMetadataSerializer::class)
sealed interface TraktHistoryMetadataBody {

    @SerialName(Id)
    val id: HistoryItemId

    val tmdbId: TmdbContentId

    val traktId: TraktContentId

    @SerialName(WatchedAt)
    val watchedAt: DateTime

    val ids: ContentIds
        get() = ContentIds(tmdbId, traktId)
}

@Serializable
private data class TraktHistoryMetadataSurrogate(
    @SerialName(TraktContentType.Episode)
    val episode: TraktEpisodeMetadataBody? = null,
    @SerialName(Id)
    val id: HistoryItemId,
    @SerialName(TraktContentType.Movie)
    val movie: TraktMovieMetadataBody? = null,
    @SerialName(TraktContentType.TvShow)
    val show: TraktTvShowMetadataBody? = null,
    @Contextual
    @SerialName(WatchedAt)
    val watchedAt: DateTime
)

private object TraktHistoryMetadataSerializer : KSerializer<TraktHistoryMetadataBody> {

    override val descriptor = TraktHistoryMetadataSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): TraktHistoryMetadataBody {
        val surrogate = TraktHistoryMetadataSurrogate.serializer().deserialize(decoder)
        return when {
            surrogate.episode != null && surrogate.show != null -> TraktEpisodeHistoryMetadataBody(
                episode = surrogate.episode,
                id = surrogate.id,
                show = surrogate.show,
                watchedAt = surrogate.watchedAt
            )
            surrogate.movie != null -> TraktMovieHistoryMetadataBody(
                id = surrogate.id,
                movie = surrogate.movie,
                watchedAt = surrogate.watchedAt
            )
            else -> error("Invalid Trakt screenplay History metadata: $surrogate")
        }
    }

    override fun serialize(encoder: Encoder, value: TraktHistoryMetadataBody) {
        val surrogate = when (value) {
            is TraktEpisodeHistoryMetadataBody -> TraktHistoryMetadataSurrogate(
                episode = value.episode,
                id = value.id,
                show = value.show,
                watchedAt = value.watchedAt
            )
            is TraktMovieHistoryMetadataBody -> TraktHistoryMetadataSurrogate(
                id = value.id,
                movie = value.movie,
                watchedAt = value.watchedAt
            )
        }
        TraktHistoryMetadataSurrogate.serializer().serialize(encoder, surrogate)
    }
}
