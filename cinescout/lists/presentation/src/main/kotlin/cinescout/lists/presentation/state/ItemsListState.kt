package cinescout.lists.presentation.state

import androidx.paging.compose.LazyPagingItems
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.resources.TextRes
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.utils.compose.Effect

data class ItemsListState(
    val errorMessage: Effect<TextRes>,
    val filter: ListFilter,
    val itemsState: ItemsState,
    val type: ScreenplayType
) {

    sealed interface ItemsState {

        data class Empty(val message: TextRes) : ItemsState

        data class Error(val message: TextRes) : ItemsState

        object Loading : ItemsState

        data class NotEmpty(val items: LazyPagingItems<ListItemUiModel>) : ItemsState
    }
}
