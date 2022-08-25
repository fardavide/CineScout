package cinescout.lists.presentation.model

import arrow.core.NonEmptyList
import cinescout.design.TextRes

sealed interface WatchlistState {

    sealed interface Data : WatchlistState {
        object Empty : Data
        data class NotEmpty(val items: NonEmptyList<WatchlistItemUiModel>) : Data
    }
    data class Error(val message: TextRes) : WatchlistState
    object Loading : WatchlistState
}
