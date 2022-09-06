package cinescout.details.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.details.presentation.model.MovieDetailsAction
import cinescout.details.presentation.model.MovieDetailsState
import cinescout.details.presentation.model.MovieDetailsUiModel
import cinescout.error.DataError
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.usecase.GetMovieExtras
import cinescout.unsupported
import cinescout.utils.android.CineScoutViewModel
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import store.Refresh

class MovieDetailsViewModel(
    movieId: TmdbMovieId,
    private val networkErrorToMessageMapper: NetworkErrorToMessageMapper,
    getMovieExtras: GetMovieExtras
) : CineScoutViewModel<MovieDetailsAction, MovieDetailsState>(MovieDetailsState.Loading) {

    init {
        viewModelScope.launch {
            getMovieExtras(movieId, refresh = Refresh.WithInterval()).mapLatest { movieExtrasEither ->
                movieExtrasEither.fold(
                    ifLeft = ::toErrorState,
                    ifRight = { movieExtras ->
                        val movie = movieExtras.movieWithDetails.movie
                        MovieDetailsState.Data(MovieDetailsUiModel(title = movie.title, tmdbId = movie.tmdbId))
                    }
                )
            }.collect { newState ->
                updateState { newState }
            }
        }
    }

    override fun submit(action: MovieDetailsAction) {
        // Noop
    }

    private fun toErrorState(dataError: DataError): MovieDetailsState.Error = when (dataError) {
        DataError.Local.NoCache -> unsupported
        is DataError.Remote -> MovieDetailsState.Error(networkErrorToMessageMapper.toMessage(dataError.networkError))
    }
}
