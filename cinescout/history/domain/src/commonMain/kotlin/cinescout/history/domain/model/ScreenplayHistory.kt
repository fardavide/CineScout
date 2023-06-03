package cinescout.history.domain.model

import cinescout.screenplay.domain.model.ids.MovieIds
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.TvShowIds

sealed interface ScreenplayHistory {

    val items: List<ScreenplayHistoryItem>
    val screenplayIds: ScreenplayIds
    val state: ScreenplayHistoryState

    companion object {

        fun empty(screenplayIds: ScreenplayIds): ScreenplayHistory = when (screenplayIds) {
            is MovieIds -> MovieHistory.empty(screenplayIds)
            is TvShowIds -> TvShowHistory.empty(screenplayIds)
        }
    }
}

data class MovieHistory(
    override val items: List<ScreenplayHistoryItem.Movie>,
    override val screenplayIds: MovieIds
) : ScreenplayHistory {

    override val state: ScreenplayHistoryState = when (items.isEmpty()) {
        true -> MovieHistoryState.Unwatched
        false -> MovieHistoryState.Watched
    }

    companion object {

        fun empty(screenplayIds: MovieIds): MovieHistory = MovieHistory(
            items = emptyList(),
            screenplayIds = screenplayIds
        )
    }
}

data class TvShowHistory(
    override val items: List<ScreenplayHistoryItem.Episode>,
    override val screenplayIds: TvShowIds,
    override val state: TvShowHistoryState
) : ScreenplayHistory {

    companion object {

        fun empty(screenplayIds: TvShowIds): TvShowHistory = TvShowHistory(
            items = emptyList(),
            screenplayIds = screenplayIds,
            state = TvShowHistoryState.Unwatched
        )
    }
}
