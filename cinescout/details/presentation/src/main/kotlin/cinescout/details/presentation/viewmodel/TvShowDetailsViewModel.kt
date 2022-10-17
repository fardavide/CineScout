package cinescout.details.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import arrow.core.getOrHandle
import arrow.core.right
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.details.presentation.mapper.TvShowDetailsUiModelMapper
import cinescout.details.presentation.model.TvShowDetailsAction
import cinescout.details.presentation.model.TvShowDetailsState
import cinescout.error.DataError
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowMedia
import cinescout.tvshows.domain.usecase.*
import cinescout.unsupported
import cinescout.utils.android.CineScoutViewModel
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import store.Refresh

class TvShowDetailsViewModel(
    private val addTvShowToWatchlist: AddTvShowToWatchlist,
    private val tvShowDetailsUiModelMapper: TvShowDetailsUiModelMapper,
    private val tvShowId: TmdbTvShowId,
    private val networkErrorToMessageMapper: NetworkErrorToMessageMapper,
    getTvShowExtras: GetTvShowExtras,
    getTvShowMedia: GetTvShowMedia,
    private val rateTvShow: RateTvShow,
    private val removeTvShowFromWatchlist: RemoveTvShowFromWatchlist
) : CineScoutViewModel<TvShowDetailsAction, TvShowDetailsState>(TvShowDetailsState.Loading) {

    init {
        viewModelScope.launch {
            combine(
                getTvShowExtras(tvShowId, refresh = Refresh.WithInterval()),
                getTvShowMedia(tvShowId, refresh = Refresh.IfExpired()).onStart { emit(DefaultTvShowMedia().right()) }
            ) { tvShowExtrasEither, tvShowMediaEither ->
                tvShowExtrasEither.fold(
                    ifLeft = ::toErrorState,
                    ifRight = { tvShowExtras ->
                        val tvShowMedia = tvShowMediaEither.getOrHandle {
                            Logger.e("Error getting TvShow media: $it")
                            DefaultTvShowMedia()
                        }
                        val uiModel = tvShowDetailsUiModelMapper.toUiModel(
                            tvShowWithExtras = tvShowExtras,
                            media = tvShowMedia
                        )
                        TvShowDetailsState.Data(tvShowDetails = uiModel)
                    }
                )
            }.collect { newState ->
                updateState { newState }
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

    private fun toErrorState(dataError: DataError): TvShowDetailsState.Error = when (dataError) {
        DataError.Local.NoCache -> unsupported
        is DataError.Remote -> TvShowDetailsState.Error(networkErrorToMessageMapper.toMessage(dataError.networkError))
    }

    private fun DefaultTvShowMedia() = TvShowMedia(
        backdrops = emptyList(),
        posters = emptyList(),
        videos = emptyList()
    )
}
