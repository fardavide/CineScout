package cinescout.history.data.remote.model

import cinescout.history.domain.model.HistoryItemId
import cinescout.screenplay.domain.model.ids.TmdbContentId
import cinescout.screenplay.domain.model.ids.TraktContentId
import korlibs.time.DateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktEpisodeMetadataBody
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

typealias TraktEpisodesHistoryMetadataResponse = List<TraktEpisodeHistoryMetadataBody>

@Serializable
@SerialName(TraktScreenplayType.Episode)
data class TraktEpisodeHistoryMetadataBody(
    @SerialName(Id)
    override val id: HistoryItemId,
    @SerialName(TraktScreenplayType.Episode)
    val episode: TraktEpisodeMetadataBody,
    @SerialName(TraktScreenplayType.TvShow)
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
