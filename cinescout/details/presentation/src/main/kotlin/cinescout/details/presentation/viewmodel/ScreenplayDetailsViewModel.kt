package cinescout.details.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import arrow.core.getOrElse
import arrow.core.right
import cinescout.details.domain.usecase.GetScreenplayWithExtras
import cinescout.details.presentation.action.ScreenplayDetailsAction
import cinescout.details.presentation.mapper.ScreenplayDetailsUiModelMapper
import cinescout.details.presentation.mapper.toUiModel
import cinescout.details.presentation.state.ScreenplayDetailsItemState
import cinescout.details.presentation.state.ScreenplayDetailsState
import cinescout.error.NetworkError
import cinescout.media.domain.model.ScreenplayMedia
import cinescout.media.domain.usecase.GetScreenplayMedia
import cinescout.network.usecase.ObserveConnectionStatus
import cinescout.rating.domain.usecase.RateScreenplay
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.utils.android.CineScoutViewModel
import cinescout.utils.compose.NetworkErrorToMessageMapper
import cinescout.watchlist.domain.usecase.AddToWatchlist
import cinescout.watchlist.domain.usecase.RemoveFromWatchlist
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
internal class ScreenplayDetailsViewModel(
    private val addToWatchlist: AddToWatchlist,
    private val detailsUiModelMapper: ScreenplayDetailsUiModelMapper,
    @InjectedParam private val screenplayId: TmdbScreenplayId,
    private val networkErrorToMessageMapper: NetworkErrorToMessageMapper,
    getScreenplayMedia: GetScreenplayMedia,
    getScreenplayWithExtras: GetScreenplayWithExtras,
    private val observeConnectionStatus: ObserveConnectionStatus,
    private val rateScreenplay: RateScreenplay,
    private val removeFromWatchlist: RemoveFromWatchlist
) : CineScoutViewModel<ScreenplayDetailsAction, ScreenplayDetailsState>(ScreenplayDetailsState.Loading) {

    init {
        viewModelScope.launch {
            combine(
                getScreenplayWithExtras(screenplayId, refresh = true),
                getScreenplayMedia(screenplayId, refresh = true).onStart { emit(DefaultMedia().right()) }
            ) { screenplayWithExtrasEither, screenplayMediaEither ->
                screenplayWithExtrasEither.fold(
                    ifLeft = ::toErrorState,
                    ifRight = { screenplayWithExtras ->
                        val movieMedia = screenplayMediaEither.getOrElse {
                            Logger.e("Error getting Movie media: $it")
                            DefaultMedia()
                        }
                        val uiModel = detailsUiModelMapper.toUiModel(
                            screenplayWithExtras = screenplayWithExtras,
                            media = movieMedia
                        )
                        ScreenplayDetailsItemState.Data(uiModel = uiModel)
                    }
                )
            }.collect { newItemState ->
                updateState { currentState ->
                    currentState.copy(itemState = newItemState)
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

    override fun submit(action: ScreenplayDetailsAction) {
        viewModelScope.launch {
            when (action) {
                ScreenplayDetailsAction.AddToWatchlist -> addToWatchlist(id = screenplayId)
                is ScreenplayDetailsAction.Rate -> rateScreenplay(id = screenplayId, action.rating)
                ScreenplayDetailsAction.RemoveFromWatchlist -> removeFromWatchlist(id = screenplayId)
            }
        }
    }

    private fun toErrorState(networkError: NetworkError): ScreenplayDetailsItemState.Error =
        ScreenplayDetailsItemState.Error(networkErrorToMessageMapper.toMessage(networkError))

    private fun DefaultMedia() = ScreenplayMedia.from(
        screenplayId = screenplayId,
        backdrops = emptyList(),
        posters = emptyList(),
        videos = emptyList()
    )
}
