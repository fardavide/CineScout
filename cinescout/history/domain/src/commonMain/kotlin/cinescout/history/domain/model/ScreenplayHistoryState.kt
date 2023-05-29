package cinescout.history.domain.model

sealed interface ScreenplayHistoryState

sealed interface MovieHistoryState : ScreenplayHistoryState {

    object Unwatched : MovieHistoryState
    object Watched : MovieHistoryState
}

sealed interface TvShowHistoryState : ScreenplayHistoryState {

    object InProgress : TvShowHistoryState
    object Unwatched : TvShowHistoryState
    object Completed : TvShowHistoryState
}
