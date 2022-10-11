package cinescout.lists.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.model.ItemsListState
import cinescout.lists.presentation.model.LikedListAction
import cinescout.movies.domain.usecase.GetAllLikedMovies
import cinescout.utils.android.CineScoutViewModel
import cinescout.utils.kotlin.nonEmptyUnsafe
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class LikedListViewModel(
    private val getAllLikedMovies: GetAllLikedMovies,
    private val listItemUiModelMapper: ListItemUiModelMapper
) : CineScoutViewModel<LikedListAction, ItemsListState>(ItemsListState.Loading) {

    init {
        viewModelScope.launch {
            getAllLikedMovies().map { movies ->
                val items = movies.map(listItemUiModelMapper::toUiModel)
                if (items.isEmpty()) ItemsListState.ItemsState.Data.Empty
                else ItemsListState.ItemsState.Data.NotEmpty(items.nonEmptyUnsafe())

            }.collect { newItemsState ->
                updateState { currentState -> currentState.copy(items = newItemsState) }
            }
        }
    }

    override fun submit(action: LikedListAction) {
        // No actions
    }
}
