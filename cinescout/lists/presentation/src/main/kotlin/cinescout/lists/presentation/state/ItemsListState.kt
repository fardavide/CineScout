package cinescout.lists.presentation.state

import arrow.core.NonEmptyList
import cinescout.design.TextRes
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.lists.presentation.model.ListType

data class ItemsListState(
    val items: ItemsState,
    val filter: ListFilter,
    val type: ListType
) {

    sealed interface ItemsState {

        sealed interface Data : ItemsState {
            object Empty : Data
            data class NotEmpty(val items: NonEmptyList<ListItemUiModel>) : Data
        }

        data class Error(val message: TextRes) : ItemsState
        object Loading : ItemsState
    }

    companion object {

        val Loading = ItemsListState(
            items = ItemsState.Loading,
            filter = ListFilter.Disliked,
            type = ListType.All
        )
    }
}
