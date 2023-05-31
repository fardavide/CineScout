package cinescout.history.domain.model

import cinescout.screenplay.domain.model.EpisodeNumber
import cinescout.screenplay.domain.model.SeasonNumber
import korlibs.time.DateTime

sealed interface ScreenplayHistoryItem {
    val id: HistoryItemId
    val watchedAt: DateTime

    data class Movie(
        override val id: HistoryItemId,
        override val watchedAt: DateTime
    ) : ScreenplayHistoryItem

    data class Episode(
        val episodeNumber: EpisodeNumber,
        override val id: HistoryItemId,
        val seasonNumber: SeasonNumber,
        override val watchedAt: DateTime
    ) : ScreenplayHistoryItem
}
