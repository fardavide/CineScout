package cinescout.details.presentation.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import arrow.core.getOrElse
import arrow.core.right
import cinescout.design.model.ConnectionStatusUiModel
import cinescout.details.domain.usecase.GetScreenplayWithExtras
import cinescout.details.presentation.action.ScreenplayDetailsAction
import cinescout.details.presentation.mapper.ScreenplayDetailsUiModelMapper
import cinescout.details.presentation.mapper.toUiModel
import cinescout.details.presentation.state.ScreenplayDetailsItemState
import cinescout.details.presentation.state.ScreenplayDetailsState
import cinescout.error.NetworkError
import cinescout.media.domain.model.ScreenplayMedia
import cinescout.media.domain.usecase.GetScreenplayMedia
import cinescout.network.model.ConnectionStatus
import cinescout.network.usecase.ObserveConnectionStatus
import cinescout.rating.domain.usecase.RateScreenplay
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.utils.compose.NetworkErrorToMessageMapper
import cinescout.watchlist.domain.usecase.AddToWatchlist
import cinescout.watchlist.domain.usecase.RemoveFromWatchlist
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class ScreenplayDetailsPresenter(
    private val addToWatchlist: AddToWatchlist,
    private val detailsUiModelMapper: ScreenplayDetailsUiModelMapper,
    private val networkErrorToMessageMapper: NetworkErrorToMessageMapper,
    private val getScreenplayMedia: GetScreenplayMedia,
    private val getScreenplayWithExtras: GetScreenplayWithExtras,
    private val observeConnectionStatus: ObserveConnectionStatus,
    private val rateScreenplay: RateScreenplay,
    private val removeFromWatchlist: RemoveFromWatchlist
) {

    @Composable
    fun models(screenplayIds: ScreenplayIds, actions: Flow<ScreenplayDetailsAction>): ScreenplayDetailsState {
        LaunchedEffect(Unit) {
            actions.collect { action ->
                when (action) {
                    ScreenplayDetailsAction.AddToWatchlist -> addToWatchlist(screenplayIds)
                    is ScreenplayDetailsAction.Rate -> rateScreenplay(screenplayIds, action.rating)
                    ScreenplayDetailsAction.RemoveFromWatchlist -> removeFromWatchlist(screenplayIds.tmdb)
                }
            }
        }

        return ScreenplayDetailsState(
            connectionStatus = connectionStatus(),
            itemState = itemState(screenplayIds)
        )
    }

    @Composable
    private fun connectionStatus(): ConnectionStatusUiModel {
        val connectionStatus by observeConnectionStatus()
            .collectAsState(initial = ConnectionStatus.AllOnline)
        return connectionStatus.toUiModel()
    }

    @Composable
    private fun itemState(screenplayIds: ScreenplayIds): ScreenplayDetailsItemState {
        val screenplayWithExtrasEither = getScreenplayWithExtras(screenplayIds, refresh = true)
            .collectAsState(initial = null)
            .value
            ?: return ScreenplayDetailsItemState.Loading

        val screenplayMediaEither by getScreenplayMedia(screenplayIds.tmdb, refresh = true)
            .collectAsState(initial = DefaultMedia(screenplayIds.tmdb).right())

        val screenplayMedia = screenplayMediaEither.getOrElse {
            Logger.e("Error getting Movie media: $it")
            DefaultMedia(screenplayIds.tmdb)
        }
        return screenplayWithExtrasEither.fold(
            ifLeft = ::toErrorState,
            ifRight = { screenplayWithExtras ->
                val uiModel = detailsUiModelMapper.toUiModel(
                    screenplayWithExtras = screenplayWithExtras,
                    media = screenplayMedia
                )
                ScreenplayDetailsItemState.Data(uiModel = uiModel)
            }
        )
    }

    private fun toErrorState(networkError: NetworkError): ScreenplayDetailsItemState.Error =
        ScreenplayDetailsItemState.Error(networkErrorToMessageMapper.toMessage(networkError))

    private fun DefaultMedia(screenplayId: TmdbScreenplayId) = ScreenplayMedia.from(
        screenplayId = screenplayId,
        backdrops = emptyList(),
        posters = emptyList(),
        videos = emptyList()
    )
}
