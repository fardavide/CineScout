package cinescout.lists.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.model.DislikedListAction
import cinescout.lists.presentation.model.ItemsListState
import cinescout.movies.domain.usecase.GetAllDislikedMovies
import cinescout.utils.android.CineScoutViewModel
import cinescout.utils.kotlin.nonEmptyUnsafe
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class DislikedListViewModel(
    private val getAllDislikedMovies: GetAllDislikedMovies,
    private val listItemUiModelMapper: ListItemUiModelMapper
) : CineScoutViewModel<DislikedListAction, ItemsListState>(ItemsListState.Loading) {

    init {
        viewModelScope.launch {
            getAllDislikedMovies().map { movies ->
                val items = movies.map(listItemUiModelMapper::toUiModel)
                if (items.isEmpty()) ItemsListState.Data.Empty
                else ItemsListState.Data.NotEmpty(items.nonEmptyUnsafe())

            }.collect { newState ->
                updateState { newState }
            }
        }
    }

    override fun submit(action: DislikedListAction) {
        // No actions
    }
}