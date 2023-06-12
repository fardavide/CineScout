package cinescout.details.presentation.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import arrow.core.Either
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
import cinescout.details.presentation.mapper.DetailsSeasonsUiModelMapper
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
import cinescout.progress.domain.model.MovieProgress
import cinescout.progress.domain.model.ScreenplayProgress
import cinescout.progress.domain.model.TvShowProgress
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
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@Factory
internal class ScreenplayDetailsPresenter(
    private val addToHistory: AddToHistory,
    private val calculateProgress: CalculateProgress,
    private val detailsUiModelMapper: ScreenplayDetailsUiModelMapper,
    private val detailsActionsUiModelMapper: DetailsActionsUiModelMapper,
    private val getScreenplayWithExtras: GetScreenplayWithExtras,
    private val getTvShowSeasonsWithEpisodes: GetTvShowSeasonsWithEpisodes,
    private val networkErrorToMessageMapper: NetworkErrorToMessageMapper,
    private val observeConnectionStatus: ObserveConnectionStatus,
    private val rateScreenplay: RateScreenplay,
    private val seasonsUiModelMapper: DetailsSeasonsUiModelMapper,
    private val toggleWatchlist: ToggleWatchlist
) {

    @Composable
    fun models(screenplayIds: ScreenplayIds, actions: Flow<ScreenplayDetailsAction>): ScreenplayDetailsState {
        LaunchedEffect(Unit) {
            actions.collect { action ->
                when (action) {
                    is ScreenplayDetailsAction.AddEpisodeToHistory ->
                        addToHistory(action.tvShowIds, action.episodeIds, action.episode)
                    is ScreenplayDetailsAction.AddMovieToHistory ->
                        addToHistory(action.movieIds)
                    is ScreenplayDetailsAction.AddSeasonToHistory ->
                        addToHistory(action.tvShowIds, action.seasonIds, action.episodes)
                    is ScreenplayDetailsAction.AddTvShowToHistory ->
                        addToHistory(action.tvShowIds, action.episodes)
                    is ScreenplayDetailsAction.Rate -> rateScreenplay(screenplayIds, action.rating)
                    ScreenplayDetailsAction.ToggleWatchlist -> toggleWatchlist(screenplayIds)
                }
            }
        }

        val progress by remember(screenplayIds) {
            progressFlow(screenplayIds).mapLatest { it.getOrNull() }
        }.collectAsState(initial = null)

        return ScreenplayDetailsState(
            actionsUiModel = actionsUiModel(screenplayIds, progress),
            connectionStatus = connectionStatus(),
            itemState = itemState(screenplayIds, progress)
        )
    }

    @Composable
    fun actionsUiModel(screenplayIds: ScreenplayIds, progress: ScreenplayProgress?): DetailsActionsUiModel {
        progress ?: return detailsActionsUiModelMapper.buildEmpty()

        val uiModelEither = remember(screenplayIds, progress) {
            getScreenplayWithExtras(
                screenplayIds,
                refresh = false,
                refreshExtras = true,
                WithPersonalRating,
                WithWatchlist
            ).map { withExtraEither ->
                withExtraEither.map { withExtra ->
                    detailsActionsUiModelMapper.toUiModel(withExtra, progress)
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
    private fun itemState(
        screenplayIds: ScreenplayIds,
        progress: ScreenplayProgress?
    ): ScreenplayDetailsItemState {
        val uiModelEither = remember(screenplayIds, progress) {
            val seasonsState = when (progress) {
                is MovieProgress -> DetailsSeasonsState.NoSeasons
                null -> DetailsSeasonsState.Loading
                is TvShowProgress -> DetailsSeasonsState.Data(seasonsUiModelMapper.toUiModel(progress))
            }

            getScreenplayWithExtras(
                screenplayIds,
                refresh = true,
                refreshExtras = true,
                WithCredits,
                WithGenres,
                WithMedia,
                WithPersonalRating
            ).map { either -> either.map { detailsUiModelMapper.toUiModel(it, seasonsState) } }
        }
            .collectAsState(initial = null)
            .value
            ?: return ScreenplayDetailsItemState.Loading

        return uiModelEither.fold(
            ifLeft = ::toErrorState,
            ifRight = ScreenplayDetailsItemState::Data
        )
    }

    private fun progressFlow(screenplayIds: ScreenplayIds): Flow<Either<NetworkError, ScreenplayProgress>> {
        val screenplayWithExtraFlow = getScreenplayWithExtras(
            screenplayIds,
            refresh = false,
            refreshExtras = true,
            WithHistory
        )
        return when (screenplayIds) {
            is MovieIds -> screenplayWithExtraFlow.map { withExtraEither ->
                withExtraEither.map { withExtra ->
                    calculateProgress(
                        movie = withExtra.screenplay as Movie,
                        history = withExtra.history as MovieHistory
                    )
                }
            }

            is TvShowIds -> combine(
                screenplayWithExtraFlow,
                getTvShowSeasonsWithEpisodes(screenplayIds, refresh = true)
            ) { withExtraEither, seasonsEither ->
                either {
                    val withExtra = withExtraEither.bind()
                    val seasons = seasonsEither.bind()
                    calculateProgress(
                        tvShow = withExtra.screenplay as TvShow,
                        history = withExtra.history as TvShowHistory,
                        seasons = seasons
                    )
                }
            }
        }
    }

    private fun toErrorState(networkError: NetworkError): ScreenplayDetailsItemState.Error =
        ScreenplayDetailsItemState.Error(networkErrorToMessageMapper.toMessage(networkError))
}
