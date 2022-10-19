package cinescout.lists.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.model.DislikedListAction
import cinescout.lists.presentation.model.ItemsListState
import cinescout.lists.presentation.model.ListType
import cinescout.movies.domain.usecase.GetAllDislikedMovies
import cinescout.tvshows.domain.usecase.GetAllDislikedTvShows
import cinescout.utils.android.CineScoutViewModel
import cinescout.utils.kotlin.nonEmptyUnsafe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

internal class DislikedListViewModel(
    private val getAllDislikedMovies: GetAllDislikedMovies,
    private val getAllDislikedTvShows: GetAllDislikedTvShows,
    private val listItemUiModelMapper: ListItemUiModelMapper
) : CineScoutViewModel<DislikedListAction, ItemsListState>(ItemsListState.Loading) {

    private val listTypeState = MutableStateFlow(ListType.All)

    init {
        viewModelScope.launch {
            combine(
                getAllDislikedMovies(),
                getAllDislikedTvShows(),
                listTypeState
            ) { movies, tvShows, listType ->
                val uiModels = when (listType) {
                    ListType.All -> movies.map(listItemUiModelMapper::toUiModel) +
                        tvShows.map(listItemUiModelMapper::toUiModel)
                    ListType.Movies -> movies.map(listItemUiModelMapper::toUiModel)
                    ListType.TvShows -> tvShows.map(listItemUiModelMapper::toUiModel)
                }
                if (uiModels.isEmpty()) ItemsListState.ItemsState.Data.Empty
                else ItemsListState.ItemsState.Data.NotEmpty(uiModels.nonEmptyUnsafe())
            }.collect { newItemsState ->
                updateState { currentState -> currentState.copy(items = newItemsState) }
            }
        }
    }

    override fun submit(action: DislikedListAction) {
        when (action) {
            is DislikedListAction.SelectListType -> onSelectListType(action.listType)
        }
    }

    private fun onSelectListType(listType: ListType) {
        viewModelScope.launch {
            listTypeState.emit(listType)
        }
    }
}
