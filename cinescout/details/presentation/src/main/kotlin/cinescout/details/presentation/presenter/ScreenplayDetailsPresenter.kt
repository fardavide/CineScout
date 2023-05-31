package cinescout.details.presentation.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import cinescout.design.model.ConnectionStatusUiModel
import cinescout.details.domain.model.WithCredits
import cinescout.details.domain.model.WithGenres
import cinescout.details.domain.model.WithMedia
import cinescout.details.domain.model.WithPersonalRating
import cinescout.details.domain.model.WithWatchlist
import cinescout.details.domain.usecase.GetScreenplayWithExtras
import cinescout.details.presentation.action.ScreenplayDetailsAction
import cinescout.details.presentation.mapper.ScreenplayDetailsUiModelMapper
import cinescout.details.presentation.mapper.toUiModel
import cinescout.details.presentation.state.ScreenplayDetailsItemState
import cinescout.details.presentation.state.ScreenplayDetailsState
import cinescout.error.NetworkError
import cinescout.network.model.ConnectionStatus
import cinescout.network.usecase.ObserveConnectionStatus
import cinescout.rating.domain.usecase.RateScreenplay
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.utils.compose.NetworkErrorToMessageMapper
import cinescout.watchlist.domain.usecase.AddToWatchlist
import cinescout.watchlist.domain.usecase.RemoveFromWatchlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
internal class ScreenplayDetailsPresenter(
    private val addToWatchlist: AddToWatchlist,
    private val detailsUiModelMapper: ScreenplayDetailsUiModelMapper,
    private val networkErrorToMessageMapper: NetworkErrorToMessageMapper,
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
        val screenplayWithExtrasEither = remember(screenplayIds) {
            getScreenplayWithExtras(
                screenplayIds,
                refresh = true,
                refreshExtras = true,
                WithCredits,
                WithGenres,
                WithMedia,
                WithPersonalRating,
                WithWatchlist
            ).map { either -> either.map(detailsUiModelMapper::toUiModel) }
        }
            .collectAsState(initial = null)
            .value
            ?: return ScreenplayDetailsItemState.Loading

        return screenplayWithExtrasEither.fold(
            ifLeft = ::toErrorState,
            ifRight = ScreenplayDetailsItemState::Data
        )
    }

    private fun toErrorState(networkError: NetworkError): ScreenplayDetailsItemState.Error =
        ScreenplayDetailsItemState.Error(networkErrorToMessageMapper.toMessage(networkError))
}
