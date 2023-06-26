package cinescout.history.data.remote.model

import cinescout.history.domain.model.HistoryItemId
import cinescout.screenplay.domain.model.id.TmdbScreenplayId
import cinescout.screenplay.domain.model.id.TraktScreenplayId
import korlibs.time.DateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody

typealias TraktMoviesHistoryMetadataResponse = List<TraktMovieHistoryMetadataBody>

@Serializable
@SerialName(TraktContentType.Movie)
data class TraktMovieHistoryMetadataBody(
    @SerialName(Id)
    override val id: HistoryItemId,
    @SerialName(TraktContentType.Movie)
    val movie: TraktMovieMetadataBody,
    @Contextual
    @SerialName(WatchedAt)
    override val watchedAt: DateTime
) : TraktHistoryMetadataBody {

    override val tmdbId: TmdbScreenplayId
        get() = movie.ids.tmdb

    override val traktId: TraktScreenplayId
        get() = movie.ids.trakt
}
