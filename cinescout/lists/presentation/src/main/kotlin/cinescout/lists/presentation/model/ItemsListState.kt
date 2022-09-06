package cinescout.lists.presentation.model

import arrow.core.NonEmptyList
import cinescout.design.TextRes

sealed interface ItemsListState {

    sealed interface Data : ItemsListState {
        object Empty : Data
        data class NotEmpty(val items: NonEmptyList<ListItemUiModel>) : Data
    }
    data class Error(val message: TextRes) : ItemsListState
    object Loading : ItemsListState
}
