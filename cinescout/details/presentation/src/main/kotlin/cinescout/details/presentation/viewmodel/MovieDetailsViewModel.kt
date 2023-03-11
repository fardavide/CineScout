package cinescout.details.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import arrow.core.getOrElse
import arrow.core.right
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.details.presentation.mapper.MovieDetailsUiModelMapper
import cinescout.details.presentation.mapper.toUiModel
import cinescout.details.presentation.model.MovieDetailsAction
import cinescout.details.presentation.state.MovieDetailsMovieState
import cinescout.details.presentation.state.MovieDetailsState
import cinescout.error.DataError
import cinescout.error.NetworkError
import cinescout.movies.domain.model.MovieMedia
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.usecase.AddMovieToWatchlist
import cinescout.movies.domain.usecase.GetMovieExtras
import cinescout.movies.domain.usecase.GetMovieMedia
import cinescout.movies.domain.usecase.RateMovie
import cinescout.movies.domain.usecase.RemoveMovieFromWatchlist
import cinescout.network.usecase.ObserveConnectionStatus
import cinescout.unsupported
import cinescout.utils.android.CineScoutViewModel
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam
import store.Refresh

@KoinViewModel
internal class MovieDetailsViewModel(
    private val addMovieToWatchlist: AddMovieToWatchlist,
    private val movieDetailsUiModelMapper: MovieDetailsUiModelMapper,
    @InjectedParam private val movieId: TmdbMovieId,
    private val networkErrorToMessageMapper: NetworkErrorToMessageMapper,
    getMovieExtras: GetMovieExtras,
    getMovieMedia: GetMovieMedia,
    private val observeConnectionStatus: ObserveConnectionStatus,
    private val rateMovie: RateMovie,
    private val removeMovieFromWatchlist: RemoveMovieFromWatchlist
) : CineScoutViewModel<MovieDetailsAction, MovieDetailsState>(MovieDetailsState.Loading) {

    init {
        viewModelScope.launch {
            combine(
                getMovieExtras(movieId, refresh = true),
                getMovieMedia(movieId, refresh = Refresh.IfExpired()).onStart { emit(DefaultMovieMedia().right()) }
            ) { movieExtrasEither, movieMediaEither ->
                movieExtrasEither.fold(
                    ifLeft = ::toErrorState,
                    ifRight = { movieExtras ->
                        val movieMedia = movieMediaEither.getOrElse {
                            Logger.e("Error getting Movie media: $it")
                            DefaultMovieMedia()
                        }
                        val uiModel = movieDetailsUiModelMapper.toUiModel(
                            movieWithExtras = movieExtras,
                            media = movieMedia
                        )
                        MovieDetailsMovieState.Data(movieDetails = uiModel)
                    }
                )
            }.collect { newMovieState ->
                updateState { currentState ->
                    currentState.copy(movieState = newMovieState)
                }
            }
        }

        viewModelScope.launch {
            observeConnectionStatus().collectLatest { connectionStatus ->
                updateState { currentState ->
                    currentState.copy(connectionStatus = connectionStatus.toUiModel())
                }
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

    private fun toErrorState(dataError: DataError): MovieDetailsMovieState.Error = when (dataError) {
        DataError.Local.NoCache -> unsupported
        is DataError.Remote ->
            MovieDetailsMovieState.Error(networkErrorToMessageMapper.toMessage(dataError.networkError))
    }

    private fun toErrorState(networkError: NetworkError): MovieDetailsMovieState.Error =
        MovieDetailsMovieState.Error(networkErrorToMessageMapper.toMessage(networkError))

    private fun DefaultMovieMedia() = MovieMedia(
        backdrops = emptyList(),
        posters = emptyList(),
        videos = emptyList()
    )
}
