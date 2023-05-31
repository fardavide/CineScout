package cinescout.history.data.remote.model

import cinescout.history.domain.model.HistoryItemId
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.model.ids.TraktScreenplayId
import korlibs.time.DateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody
import screenplay.data.remote.trakt.model.TraktScreenplayType

typealias TraktMoviesHistoryMetadataResponse = List<TraktMovieHistoryMetadataBody>

@Serializable
@SerialName(TraktScreenplayType.Movie)
data class TraktMovieHistoryMetadataBody(
    @SerialName(Id)
    override val id: HistoryItemId,
    @SerialName(TraktScreenplayType.Movie)
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
