package cinescout.history.domain.model

import cinescout.screenplay.domain.model.ScreenplayIds

sealed interface ScreenplayHistory {

    val items: List<ScreenplayHistoryItem>
    val screenplayIds: ScreenplayIds
    val state: ScreenplayHistoryState
}

data class MovieHistory(
    override val items: List<ScreenplayHistoryItem.Movie>,
    override val screenplayIds: ScreenplayIds.Movie
) : ScreenplayHistory {

    override val state: ScreenplayHistoryState = when (items.isEmpty()) {
        true -> MovieHistoryState.Unwatched
        false -> MovieHistoryState.Watched
    }
}

data class TvShowHistory(
    override val items: List<ScreenplayHistoryItem.Episode>,
    override val screenplayIds: ScreenplayIds.TvShow,
    override val state: TvShowHistoryState
) : ScreenplayHistory
