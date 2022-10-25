package cinescout.lists.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.model.ItemsListState
import cinescout.lists.presentation.model.LikedListAction
import cinescout.lists.presentation.model.ListType
import cinescout.movies.domain.usecase.GetAllLikedMovies
import cinescout.tvshows.domain.usecase.GetAllLikedTvShows
import cinescout.utils.android.CineScoutViewModel
import cinescout.utils.kotlin.nonEmptyUnsafe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

internal class LikedListViewModel(
    private val getAllLikedMovies: GetAllLikedMovies,
    private val getAllLikedTvShows: GetAllLikedTvShows,
    private val listItemUiModelMapper: ListItemUiModelMapper
) : CineScoutViewModel<LikedListAction, ItemsListState>(ItemsListState.Loading) {

    private val listTypeState = MutableStateFlow(ListType.All)

    init {
        viewModelScope.launch {
            combine(
                getAllLikedMovies(),
                getAllLikedTvShows(),
                listTypeState
            ) { movies, tvShows, listType ->
                val uiModels = when (listType) {
                    ListType.All -> movies.map(listItemUiModelMapper::toUiModel) +
                        tvShows.map(listItemUiModelMapper::toUiModel)
                    ListType.Movies -> movies.map(listItemUiModelMapper::toUiModel)
                    ListType.TvShows -> tvShows.map(listItemUiModelMapper::toUiModel)
                }
                if (uiModels.isEmpty()) ItemsListState.ItemsState.Data.Empty to listType
                else ItemsListState.ItemsState.Data.NotEmpty(uiModels.nonEmptyUnsafe()) to listType
            }.collect { (newItemsState, listType) ->
                updateState { currentState -> currentState.copy(items = newItemsState, type = listType) }
            }
        }
    }

    override fun submit(action: LikedListAction) {
        when (action) {
            is LikedListAction.SelectListType -> onSelectListType(action.listType)
        }
    }

    private fun onSelectListType(listType: ListType) {
        viewModelScope.launch {
            listTypeState.emit(listType)
        }
    }
}
