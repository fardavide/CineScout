package cinescout.details.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import arrow.core.getOrElse
import arrow.core.right
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.details.presentation.mapper.TvShowDetailsUiModelMapper
import cinescout.details.presentation.mapper.toUiModel
import cinescout.details.presentation.model.TvShowDetailsAction
import cinescout.details.presentation.state.TvShowDetailsState
import cinescout.details.presentation.state.TvShowDetailsTvShowState
import cinescout.error.NetworkError
import cinescout.network.usecase.ObserveConnectionStatus
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowMedia
import cinescout.tvshows.domain.usecase.AddTvShowToWatchlist
import cinescout.tvshows.domain.usecase.GetTvShowExtras
import cinescout.tvshows.domain.usecase.GetTvShowMedia
import cinescout.tvshows.domain.usecase.RateTvShow
import cinescout.tvshows.domain.usecase.RemoveTvShowFromWatchlist
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
internal class TvShowDetailsViewModel(
    private val addTvShowToWatchlist: AddTvShowToWatchlist,
    getTvShowExtras: GetTvShowExtras,
    getTvShowMedia: GetTvShowMedia,
    private val networkErrorToMessageMapper: NetworkErrorToMessageMapper,
    private val observeConnectionStatus: ObserveConnectionStatus,
    private val rateTvShow: RateTvShow,
    private val removeTvShowFromWatchlist: RemoveTvShowFromWatchlist,
    private val tvShowDetailsUiModelMapper: TvShowDetailsUiModelMapper,
    @InjectedParam private val tvShowId: TmdbTvShowId
) : CineScoutViewModel<TvShowDetailsAction, TvShowDetailsState>(TvShowDetailsState.Loading) {

    init {
        viewModelScope.launch {
            combine(
                getTvShowExtras(tvShowId, refresh = true),
                getTvShowMedia(tvShowId, refresh = Refresh.IfExpired()).onStart { emit(DefaultTvShowMedia().right()) }
            ) { tvShowExtrasEither, tvShowMediaEither ->
                tvShowExtrasEither.fold(
                    ifLeft = ::toErrorState,
                    ifRight = { tvShowExtras ->
                        val tvShowMedia = tvShowMediaEither.getOrElse {
                            Logger.e("Error getting TvShow media: $it")
                            DefaultTvShowMedia()
                        }
                        val uiModel = tvShowDetailsUiModelMapper.toUiModel(
                            tvShowWithExtras = tvShowExtras,
                            media = tvShowMedia
                        )
                        TvShowDetailsTvShowState.Data(tvShowDetails = uiModel)
                    }
                )
            }.collect { newTvShowState ->
                updateState { currentState ->
                    currentState.copy(tvShowState = newTvShowState)
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

    override fun submit(action: TvShowDetailsAction) {
        viewModelScope.launch {
            when (action) {
                TvShowDetailsAction.AddToWatchlist -> addTvShowToWatchlist(tvShowId = tvShowId)
                is TvShowDetailsAction.RateTvShow -> rateTvShow(tvShowId = tvShowId, action.rating)
                TvShowDetailsAction.RemoveFromWatchlist -> removeTvShowFromWatchlist(tvShowId = tvShowId)
            }
        }
    }

    private fun toErrorState(networkError: NetworkError): TvShowDetailsTvShowState.Error =
        TvShowDetailsTvShowState.Error(networkErrorToMessageMapper.toMessage(networkError))

    private fun DefaultTvShowMedia() = TvShowMedia(
        backdrops = emptyList(),
        posters = emptyList(),
        videos = emptyList()
    )
}
