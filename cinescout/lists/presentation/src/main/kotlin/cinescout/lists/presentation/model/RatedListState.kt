package cinescout.lists.presentation.model

import arrow.core.NonEmptyList
import cinescout.design.TextRes

sealed interface RatedListState {

    sealed interface Data : RatedListState {
        object Empty : Data
        data class NotEmpty(val items: NonEmptyList<ListItemUiModel>) : Data
    }
    data class Error(val message: TextRes) : RatedListState
    object Loading : RatedListState
}
