package cinescout.search.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.common.model.TmdbPosterImage
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.error.DataError
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.usecase.AddMovieToLikedList
import cinescout.search.domain.usecase.SearchMovies
import cinescout.search.presentation.model.SearchLikeMovieAction
import cinescout.search.presentation.model.SearchLikedMovieState
import cinescout.search.presentation.model.SearchLikedMovieUiModel
import cinescout.unsupported
import cinescout.utils.android.CineScoutViewModel
import cinescout.utils.kotlin.nonEmpty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class SearchLikedMovieViewModel(
    private val addMovieToLikedList: AddMovieToLikedList,
    private val networkErrorToMessageMapper: NetworkErrorToMessageMapper,
    private val searchMovies: SearchMovies
) : CineScoutViewModel<SearchLikeMovieAction, SearchLikedMovieState>(initialState = SearchLikedMovieState.Idle) {

    private val searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            searchQuery
                .debounce(300.milliseconds)
                .onEach { query ->
                    updateState { currentState ->
                        val result = currentState.result.onNewQuery(query)
                        currentState.copy(query = query, result = result)
                    }
                }
                .filterNot { query -> query.isBlank() }
                .flatMapLatest { query -> searchMovies(query) }
                .map { moviesEither ->
                    moviesEither.fold(
                        ifLeft = ::toSearchResultError,
                        ifRight = { toSearchResult(it.data) }
                    )
                }
                .collect { searchResult ->
                    updateState { currentState ->
                        currentState.copy(result = searchResult)
                    }
                }
        }
    }

    override fun submit(action: SearchLikeMovieAction) {
        when (action) {
            is SearchLikeMovieAction.LikeMovie -> likeMovie(action.movieId)
            is SearchLikeMovieAction.Search -> updateSearchQuery(action.query)
        }
    }

    private fun likeMovie(movieId: TmdbMovieId) {
        viewModelScope.launch {
            addMovieToLikedList(movieId)
        }
    }

    private fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            searchQuery.emit(query)
        }
    }

    private fun toSearchResultError(dataError: DataError): SearchLikedMovieState.SearchResult.Error =
        when (dataError) {
            DataError.Local.NoCache -> unsupported
            is DataError.Remote -> {
                val message = networkErrorToMessageMapper.toMessage(dataError.networkError)
                SearchLikedMovieState.SearchResult.Error(message = message)
            }
        }

    private fun toSearchResult(movies: List<Movie>): SearchLikedMovieState.SearchResult =
        movies.nonEmpty().fold(
            ifEmpty = { SearchLikedMovieState.SearchResult.NoResults },
            ifSome = { nonEmptyList ->
                val uiModels = nonEmptyList.map { movie ->
                    SearchLikedMovieUiModel(
                        movieId = movie.tmdbId,
                        title = movie.title,
                        posterUrl = movie.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.SMALL)
                    )
                }
                SearchLikedMovieState.SearchResult.Data(uiModels)
            }
        )
}
