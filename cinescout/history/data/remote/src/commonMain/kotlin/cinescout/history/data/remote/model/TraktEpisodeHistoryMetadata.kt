package cinescout.history.data.remote.model

import cinescout.history.domain.model.HistoryItemId
import cinescout.screenplay.domain.model.id.TmdbContentId
import cinescout.screenplay.domain.model.id.TraktContentId
import korlibs.time.DateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktEpisodeMetadataBody
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

@Serializable
@SerialName(TraktContentType.Episode)
data class TraktEpisodeHistoryMetadataBody(

    @SerialName(Id)
    override val id: HistoryItemId,

    @SerialName(TraktContentType.Episode)
    val episode: TraktEpisodeMetadataBody,

    @SerialName(TraktContentType.TvShow)
    val show: TraktTvShowMetadataBody,

    @Contextual
    @SerialName(WatchedAt)
    override val watchedAt: DateTime

) : TraktHistoryMetadataBody {

    override val tmdbId: TmdbContentId
        get() = episode.ids.tmdb

    override val traktId: TraktContentId
        get() = episode.ids.trakt
}
