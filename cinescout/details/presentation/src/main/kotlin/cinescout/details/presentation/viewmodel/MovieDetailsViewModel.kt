package cinescout.details.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import arrow.core.getOrHandle
import arrow.core.right
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.details.presentation.mapper.MovieDetailsUiModelMapper
import cinescout.details.presentation.model.MovieDetailsAction
import cinescout.details.presentation.model.MovieDetailsState
import cinescout.error.DataError
import cinescout.movies.domain.model.MovieMedia
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.usecase.AddMovieToWatchlist
import cinescout.movies.domain.usecase.GetMovieExtras
import cinescout.movies.domain.usecase.GetMovieMedia
import cinescout.movies.domain.usecase.RateMovie
import cinescout.movies.domain.usecase.RemoveMovieFromWatchlist
import cinescout.unsupported
import cinescout.utils.android.CineScoutViewModel
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import store.Refresh

class MovieDetailsViewModel(
    private val addMovieToWatchlist: AddMovieToWatchlist,
    private val movieDetailsUiModelMapper: MovieDetailsUiModelMapper,
    private val movieId: TmdbMovieId,
    private val networkErrorToMessageMapper: NetworkErrorToMessageMapper,
    getMovieExtras: GetMovieExtras,
    getMovieMedia: GetMovieMedia,
    private val rateMovie: RateMovie,
    private val removeMovieFromWatchlist: RemoveMovieFromWatchlist
) : CineScoutViewModel<MovieDetailsAction, MovieDetailsState>(MovieDetailsState.Loading) {

    init {
        viewModelScope.launch {
            combine(
                getMovieExtras(movieId, refresh = Refresh.WithInterval()),
                getMovieMedia(movieId, refresh = Refresh.IfExpired()).onStart { emit(DefaultMovieMedia().right()) }
            ) { movieExtrasEither, movieMediaEither ->
                movieExtrasEither.fold(
                    ifLeft = ::toErrorState,
                    ifRight = { movieExtras ->
                        val movieMedia = movieMediaEither.getOrHandle {
                            Logger.e("Error getting Movie media: $it")
                            DefaultMovieMedia()
                        }
                        val uiModel = movieDetailsUiModelMapper.toUiModel(
                            movieWithExtras = movieExtras,
                            media = movieMedia
                        )
                        MovieDetailsState.Data(movieDetails = uiModel)
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

    private fun DefaultMovieMedia() = MovieMedia(
        backdrops = emptyList(),
        posters = emptyList(),
        videos = emptyList()
    )
}
