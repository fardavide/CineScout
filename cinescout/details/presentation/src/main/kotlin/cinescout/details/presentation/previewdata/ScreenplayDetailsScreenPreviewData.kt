package cinescout.details.presentation.previewdata

import cinescout.design.model.ConnectionStatusUiModel
import cinescout.design.util.PreviewDataProvider
import cinescout.details.presentation.sample.ScreenplayDetailsUiModelSample
import cinescout.details.presentation.state.ScreenplayDetailsItemState
import cinescout.details.presentation.state.ScreenplayDetailsState
import cinescout.resources.R.string
import cinescout.resources.TextRes

object ScreenplayDetailsScreenPreviewData {

    val Inception = ScreenplayDetailsState(
        itemState = ScreenplayDetailsItemState.Data(
            ScreenplayDetailsUiModelSample.Inception
        ),
        connectionStatus = ConnectionStatusUiModel.AllConnected
    )
    val Loading = ScreenplayDetailsState.Loading
    val TheWolfOfWallStreet = ScreenplayDetailsState(
        itemState = ScreenplayDetailsItemState.Data(
            ScreenplayDetailsUiModelSample.TheWolfOfWallStreet
        ),
        connectionStatus = ConnectionStatusUiModel.AllConnected
    )
    val TmdbOffline = ScreenplayDetailsState(
        itemState = ScreenplayDetailsItemState.Error(TextRes(string.network_error_no_network)),
        connectionStatus = ConnectionStatusUiModel.TmdbOffline
    )
}

class ScreenplayDetailsScreenPreviewDataProvider : PreviewDataProvider<ScreenplayDetailsState>(
    ScreenplayDetailsScreenPreviewData.Inception,
    ScreenplayDetailsScreenPreviewData.TheWolfOfWallStreet,
    ScreenplayDetailsScreenPreviewData.TmdbOffline,
    ScreenplayDetailsScreenPreviewData.Loading
)
