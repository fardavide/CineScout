package cinescout.details.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.details.presentation.mapper.MovieDetailsUiModelMapper
import cinescout.details.presentation.model.MovieDetailsAction
import cinescout.details.presentation.model.MovieDetailsState
import cinescout.error.DataError
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.usecase.AddMovieToWatchlist
import cinescout.movies.domain.usecase.GetMovieExtras
import cinescout.movies.domain.usecase.RateMovie
import cinescout.movies.domain.usecase.RemoveMovieFromWatchlist
import cinescout.unsupported
import cinescout.utils.android.CineScoutViewModel
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import store.Refresh

class MovieDetailsViewModel(
    private val addMovieToWatchlist: AddMovieToWatchlist,
    private val movieDetailsUiModelMapper: MovieDetailsUiModelMapper,
    private val movieId: TmdbMovieId,
    private val networkErrorToMessageMapper: NetworkErrorToMessageMapper,
    getMovieExtras: GetMovieExtras,
    private val rateMovie: RateMovie,
    private val removeMovieFromWatchlist: RemoveMovieFromWatchlist
) : CineScoutViewModel<MovieDetailsAction, MovieDetailsState>(MovieDetailsState.Loading) {

    init {
        viewModelScope.launch {
            getMovieExtras(movieId, refresh = Refresh.WithInterval()).mapLatest { movieExtrasEither ->
                movieExtrasEither.fold(
                    ifLeft = ::toErrorState,
                    ifRight = { movieExtras ->
                        MovieDetailsState.Data(movieDetails = movieDetailsUiModelMapper.toUiModel(movieExtras))
                    }
                )
            }.collect { newState ->
                updateState { newState }
            }
        }
    }

    override fun submit(action: MovieDetailsAction) {
        viewModelScope.launch {
            when (action) {
                MovieDetailsAction.AddToWatchlist -> addMovieToWatchlist(movieId = movieId)
                is MovieDetailsAction.RateMovie -> rateMovie(movieId = movieId, action.rating)
                MovieDetailsAction.RemoveFromWatchlist -> removeMovieFromWatchlist(movieId = movieId)
            }
        }
    }

    private fun toErrorState(dataError: DataError): MovieDetailsState.Error = when (dataError) {
        DataError.Local.NoCache -> unsupported
        is DataError.Remote -> MovieDetailsState.Error(networkErrorToMessageMapper.toMessage(dataError.networkError))
    }
}
