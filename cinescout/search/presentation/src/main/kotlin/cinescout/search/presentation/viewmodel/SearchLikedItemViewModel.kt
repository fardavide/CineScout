package cinescout.search.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.common.model.TmdbPosterImage
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.error.DataError
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.usecase.AddMovieToLikedList
import cinescout.search.domain.usecase.SearchMovies
import cinescout.search.domain.usecase.SearchTvShows
import cinescout.search.presentation.model.SearchLikeItemAction
import cinescout.search.presentation.model.SearchLikedItemEvent
import cinescout.search.presentation.model.SearchLikedItemId
import cinescout.search.presentation.model.SearchLikedItemOperation
import cinescout.search.presentation.model.SearchLikedItemState
import cinescout.search.presentation.model.SearchLikedItemType
import cinescout.search.presentation.model.SearchLikedItemUiModel
import cinescout.search.presentation.reducer.SearchLikedItemReducer
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.usecase.AddTvShowToLikedList
import cinescout.unsupported
import cinescout.utils.android.CineScoutViewModel
import cinescout.utils.android.Reducer
import cinescout.utils.kotlin.combineToPair
import cinescout.utils.kotlin.nonEmpty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class SearchLikedItemViewModel(
    private val addMovieToLikedList: AddMovieToLikedList,
    private val addTvShowToLikedList: AddTvShowToLikedList,
    private val networkErrorToMessageMapper: NetworkErrorToMessageMapper,
    reducer: SearchLikedItemReducer,
    private val searchMovies: SearchMovies,
    private val searchTvShows: SearchTvShows
) : CineScoutViewModel<SearchLikeItemAction, SearchLikedItemState>(initialState = SearchLikedItemState.Idle),
    Reducer<SearchLikedItemState, SearchLikedItemOperation> by reducer {

    private val searchQuery = MutableStateFlow("")
    private val type = MutableStateFlow(SearchLikedItemType.Movies)

    init {
        viewModelScope.launch {
            combineToPair(searchQuery, type)
                .debounce(300.milliseconds)
                .onEach { (query, _) ->
                    updateState { currentState ->
                        currentState.reduce(SearchLikedItemEvent.QueryUpdated(query = query))
                    }
                }
                .filterNot { (query, _) -> query.isBlank() }
                .flatMapLatest { (query, type) ->
                    when (type) {
                        SearchLikedItemType.Movies -> searchMovies(query).map { moviesEither ->
                            moviesEither.fold(
                                ifLeft = ::toSearchResultError,
                                ifRight = { toMoviesSearchResult(it.data) }
                            )
                        }
                        SearchLikedItemType.TvShows -> searchTvShows(query).map { tvShowsEither ->
                            tvShowsEither.fold(
                                ifLeft = ::toSearchResultError,
                                ifRight = { toTvShowsSearchResult(it.data) }
                            )
                        }
                    }
                }
                .collect { searchResult ->
                    updateState { currentState ->
                        currentState.reduce(SearchLikedItemEvent.SearchResult(result = searchResult))
                    }
                }
        }
    }

    override fun submit(action: SearchLikeItemAction) {
        when (action) {
            is SearchLikeItemAction.LikeItem -> likeItem(action.itemId)
            is SearchLikeItemAction.Search -> updateSearchQuery(action.query)
            is SearchLikeItemAction.SelectItemType -> type.value = action.itemType
        }
    }

    private fun likeItem(itemId: SearchLikedItemId) {
        viewModelScope.launch {
            when (itemId) {
                is SearchLikedItemId.Movie -> addMovieToLikedList(itemId.tmdbMovieId)
                is SearchLikedItemId.TvShow -> addTvShowToLikedList(itemId.tmdbTvShowId)
            }
        }
    }

    private fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            searchQuery.emit(query)
        }
    }

    private fun toSearchResultError(dataError: DataError): SearchLikedItemState.SearchResult.Error =
        when (dataError) {
            DataError.Local.NoCache -> unsupported
            is DataError.Remote -> {
                val message = networkErrorToMessageMapper.toMessage(dataError.networkError)
                SearchLikedItemState.SearchResult.Error(message = message)
            }
        }

    private fun toMoviesSearchResult(movies: List<Movie>): SearchLikedItemState.SearchResult =
        movies.nonEmpty().fold(
            ifEmpty = { SearchLikedItemState.SearchResult.NoResults },
            ifSome = { nonEmptyList ->
                val uiModels = nonEmptyList.map { movie ->
                    SearchLikedItemUiModel(
                        movieId = movie.tmdbId,
                        title = movie.title,
                        posterUrl = movie.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.SMALL)
                    )
                }
                SearchLikedItemState.SearchResult.Data(uiModels)
            }
        )

    private fun toTvShowsSearchResult(tvShows: List<TvShow>): SearchLikedItemState.SearchResult =
        tvShows.nonEmpty().fold(
            ifEmpty = { SearchLikedItemState.SearchResult.NoResults },
            ifSome = { nonEmptyList ->
                val uiModels = nonEmptyList.map { tvShow ->
                    SearchLikedItemUiModel(
                        tvShowId = tvShow.tmdbId,
                        title = tvShow.title,
                        posterUrl = tvShow.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.SMALL)
                    )
                }
                SearchLikedItemState.SearchResult.Data(uiModels)
            }
        )
}
