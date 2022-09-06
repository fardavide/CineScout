package cinescout.lists.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.error.DataError
import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.model.DislikedListAction
import cinescout.lists.presentation.model.ItemsListState
import cinescout.movies.domain.usecase.GetAllDislikedMovies
import cinescout.unsupported
import cinescout.utils.android.CineScoutViewModel
import cinescout.utils.kotlin.nonEmptyUnsafe
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class DislikedListViewModel(
    private val errorToMessageMapper: NetworkErrorToMessageMapper,
    private val getAllDislikedMovies: GetAllDislikedMovies,
    private val listItemUiModelMapper: ListItemUiModelMapper
) : CineScoutViewModel<DislikedListAction, ItemsListState>(ItemsListState.Loading) {

    init {
        viewModelScope.launch {
            getAllDislikedMovies().map { moviesEither ->
                moviesEither.fold(
                    ifLeft = { error -> error.toErrorState() },
                    ifRight = { movies ->
                        val items = movies.map(listItemUiModelMapper::toUiModel)
                        if (items.isEmpty()) ItemsListState.Data.Empty
                        else ItemsListState.Data.NotEmpty(items.nonEmptyUnsafe())
                    }
                )
            }.collect { newState ->
                updateState { newState }
            }
        }
    }

    private fun DataError.toErrorState(): ItemsListState.Error = when (this) {
        DataError.Local.NoCache -> unsupported
        is DataError.Remote -> ItemsListState.Error(errorToMessageMapper.toMessage(networkError))
    }

    override fun submit(action: DislikedListAction) {
        // No actions
    }
}
