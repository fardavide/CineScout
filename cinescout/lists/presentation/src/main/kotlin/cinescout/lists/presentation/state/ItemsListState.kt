package cinescout.lists.presentation.state

import androidx.paging.compose.LazyPagingItems
import arrow.core.NonEmptyList
import cinescout.design.TextRes
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.screenplay.domain.model.ScreenplayType

data class ItemsListState(
    val items: LazyPagingItems<ListItemUiModel>,
    val filter: ListFilter,
    val type: ScreenplayType
) {

    sealed interface ItemsState {

        sealed interface Data : ItemsState {
            data class Empty(val message: TextRes) : Data
            data class NotEmpty(val items: NonEmptyList<ListItemUiModel>) : Data
        }

        data class Error(val message: TextRes) : ItemsState
        object Loading : ItemsState
    }
}
