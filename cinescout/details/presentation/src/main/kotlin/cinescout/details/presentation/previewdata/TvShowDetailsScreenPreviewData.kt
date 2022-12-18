package cinescout.details.presentation.previewdata

import cinescout.design.TextRes
import cinescout.design.model.ConnectionStatusUiModel
import cinescout.design.util.PreviewDataProvider
import cinescout.details.presentation.state.TvShowDetailsState
import cinescout.details.presentation.state.TvShowDetailsTvShowState
import studio.forface.cinescout.design.R.string

object TvShowDetailsScreenPreviewData {

    val Loading = TvShowDetailsState(
        tvShowState = TvShowDetailsTvShowState.Loading,
        connectionStatus = ConnectionStatusUiModel.AllConnected
    )
    val BreakingBad = TvShowDetailsState(
        tvShowState = TvShowDetailsTvShowState.Data(
            TvShowDetailsUiModelPreviewData.BreakingBad
        ),
        connectionStatus = ConnectionStatusUiModel.AllConnected
    )
    val Grimm = TvShowDetailsState(
        tvShowState = TvShowDetailsTvShowState.Data(
            TvShowDetailsUiModelPreviewData.Grimm
        ),
        connectionStatus = ConnectionStatusUiModel.AllConnected
    )
    val TmdbOffline = TvShowDetailsState(
        tvShowState = TvShowDetailsTvShowState.Error(TextRes(string.network_error_no_network)),
        connectionStatus = ConnectionStatusUiModel.TmdbOffline
    )
}

class TvShowDetailsScreenPreviewDataProvider : PreviewDataProvider<TvShowDetailsState>(
    TvShowDetailsScreenPreviewData.Grimm,
    TvShowDetailsScreenPreviewData.BreakingBad,
    TvShowDetailsScreenPreviewData.TmdbOffline,
    TvShowDetailsScreenPreviewData.Loading
)
