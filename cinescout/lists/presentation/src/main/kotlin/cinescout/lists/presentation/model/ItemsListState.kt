package cinescout.lists.presentation.model

import arrow.core.NonEmptyList
import cinescout.design.TextRes

data class ItemsListState(
    val items: ItemsState,
    val type: Type
) {

    enum class Type {
        All, Movies, TvShows
    }

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
            type = Type.All
        )
    }
}
