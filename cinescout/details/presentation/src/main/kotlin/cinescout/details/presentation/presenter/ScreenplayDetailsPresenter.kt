package cinescout.details.presentation.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import arrow.core.raise.either
import cinescout.design.model.ConnectionStatusUiModel
import cinescout.details.domain.model.WithCredits
import cinescout.details.domain.model.WithGenres
import cinescout.details.domain.model.WithHistory
import cinescout.details.domain.model.WithMedia
import cinescout.details.domain.model.WithPersonalRating
import cinescout.details.domain.model.WithWatchlist
import cinescout.details.domain.usecase.GetScreenplayWithExtras
import cinescout.details.presentation.action.ScreenplayDetailsAction
import cinescout.details.presentation.mapper.DetailsActionsUiModelMapper
import cinescout.details.presentation.mapper.ScreenplayDetailsUiModelMapper
import cinescout.details.presentation.mapper.toUiModel
import cinescout.details.presentation.model.DetailsActionsUiModel
import cinescout.details.presentation.state.DetailsSeasonsState
import cinescout.details.presentation.state.ScreenplayDetailsItemState
import cinescout.details.presentation.state.ScreenplayDetailsState
import cinescout.error.NetworkError
import cinescout.history.domain.model.MovieHistory
import cinescout.history.domain.model.TvShowHistory
import cinescout.history.domain.usecase.AddToHistory
import cinescout.network.model.ConnectionStatus
import cinescout.network.usecase.ObserveConnectionStatus
import cinescout.progress.domain.usecase.CalculateProgress
import cinescout.rating.domain.usecase.RateScreenplay
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.ids.MovieIds
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.TvShowIds
import cinescout.seasons.domain.usecase.GetTvShowSeasonsWithEpisodes
import cinescout.utils.compose.NetworkErrorToMessageMapper
import cinescout.watchlist.domain.usecase.ToggleWatchlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
internal class ScreenplayDetailsPresenter(
    private val addToHistory: AddToHistory,
    private val calculateProgress: CalculateProgress,
    private val detailsUiModelMapper: ScreenplayDetailsUiModelMapper,
    private val detailsActionsUiModelMapper: DetailsActionsUiModelMapper,
    private val networkErrorToMessageMapper: NetworkErrorToMessageMapper,
    private val getScreenplayWithExtras: GetScreenplayWithExtras,
    private val getTvShowSeasonsWithEpisodes: GetTvShowSeasonsWithEpisodes,
    private val observeConnectionStatus: ObserveConnectionStatus,
    private val rateScreenplay: RateScreenplay,
    private val toggleWatchlist: ToggleWatchlist
) {

    @Composable
    fun models(screenplayIds: ScreenplayIds, actions: Flow<ScreenplayDetailsAction>): ScreenplayDetailsState {
        LaunchedEffect(Unit) {
            actions.collect { action ->
                when (action) {
                    ScreenplayDetailsAction.AddToHistory -> addToHistory(screenplayIds)
                    is ScreenplayDetailsAction.Rate -> rateScreenplay(screenplayIds, action.rating)
                    ScreenplayDetailsAction.ToggleWatchlist -> toggleWatchlist(screenplayIds)
                }
            }
        }

        return ScreenplayDetailsState(
            actionsUiModel = actionsUiModel(screenplayIds),
            connectionStatus = connectionStatus(),
            itemState = itemState(screenplayIds)
        )
    }

    @Composable
    fun actionsUiModel(screenplayIds: ScreenplayIds): DetailsActionsUiModel {
        val uiModelEither = remember(screenplayIds) {
            val screenplayWithExtraFlow = getScreenplayWithExtras(
                screenplayIds,
                refresh = false,
                refreshExtras = true,
                WithHistory,
                WithPersonalRating,
                WithWatchlist
            )
            when (screenplayIds) {
                is MovieIds -> screenplayWithExtraFlow.map { withExtraEither ->
                    withExtraEither.map { withExtra ->
                        val progress = calculateProgress(
                            movie = withExtra.screenplay as Movie,
                            history = withExtra.history as MovieHistory
                        )
                        detailsActionsUiModelMapper.toUiModel(withExtra, progress)
                    }
                }

                is TvShowIds -> combine(
                    screenplayWithExtraFlow,
                    getTvShowSeasonsWithEpisodes(screenplayIds, refresh = true)
                ) { withExtraEither, seasonsEither ->
                    either {
                        val withExtra = withExtraEither.bind()
                        val seasons = seasonsEither.bind()
                        val progress = calculateProgress(
                            tvShow = withExtra.screenplay as TvShow,
                            history = withExtra.history as TvShowHistory,
                            seasons = seasons
                        )
                        detailsActionsUiModelMapper.toUiModel(withExtra, progress)
                    }
                }
            }
        }.collectAsState(initial = null).value

        return uiModelEither?.getOrNull() ?: detailsActionsUiModelMapper.buildEmpty()
    }

    @Composable
    private fun connectionStatus(): ConnectionStatusUiModel {
        val connectionStatus by observeConnectionStatus()
            .collectAsState(initial = ConnectionStatus.AllOnline)
        return connectionStatus.toUiModel()
    }

    @Composable
    private fun itemState(screenplayIds: ScreenplayIds): ScreenplayDetailsItemState {
        val uiModelEither = remember(screenplayIds) {
            getScreenplayWithExtras(
                screenplayIds,
                refresh = true,
                refreshExtras = true,
                WithCredits,
                WithGenres,
                WithMedia,
                WithPersonalRating
                // TODO seasons
            ).map { either -> either.map { detailsUiModelMapper.toUiModel(it, DetailsSeasonsState.NoSeasons) } }
        }
            .collectAsState(initial = null)
            .value
            ?: return ScreenplayDetailsItemState.Loading

        return uiModelEither.fold(
            ifLeft = ::toErrorState,
            ifRight = ScreenplayDetailsItemState::Data
        )
    }

    private fun toErrorState(networkError: NetworkError): ScreenplayDetailsItemState.Error =
        ScreenplayDetailsItemState.Error(networkErrorToMessageMapper.toMessage(networkError))
}
