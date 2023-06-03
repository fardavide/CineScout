package cinescout.history.domain.model

sealed interface ScreenplayHistoryState {

    fun isStarted(): Boolean = when (this) {
        MovieHistoryState.Unwatched,
        TvShowHistoryState.Unwatched -> false
        MovieHistoryState.Watched,
        TvShowHistoryState.Completed,
        TvShowHistoryState.InProgress -> true
    }
}

sealed interface MovieHistoryState : ScreenplayHistoryState {

    object Unwatched : MovieHistoryState
    object Watched : MovieHistoryState
}

sealed interface TvShowHistoryState : ScreenplayHistoryState {

    object InProgress : TvShowHistoryState
    object Unwatched : TvShowHistoryState
    object Completed : TvShowHistoryState
}
