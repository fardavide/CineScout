package cinescout.history.domain.model

import cinescout.screenplay.domain.model.id.MovieIds
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.model.id.TvShowIds

sealed interface ScreenplayHistory {

    val items: List<ScreenplayHistoryItem>
    val screenplayIds: ScreenplayIds

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

    companion object {

        fun empty(screenplayIds: MovieIds): MovieHistory = MovieHistory(
            items = emptyList(),
            screenplayIds = screenplayIds
        )
    }
}

data class TvShowHistory(
    override val items: List<ScreenplayHistoryItem.Episode>,
    override val screenplayIds: TvShowIds
) : ScreenplayHistory {

    companion object {

        fun empty(screenplayIds: TvShowIds): TvShowHistory = TvShowHistory(
            items = emptyList(),
            screenplayIds = screenplayIds
        )
    }
}
