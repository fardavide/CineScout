package cinescout.lists.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import arrow.core.nonEmptyListOf
import cinescout.design.TextRes
import cinescout.lists.presentation.model.WatchlistState
import studio.forface.cinescout.design.R.string

object WatchlistScreenPreviewData {

    val EmptyWatchlist = WatchlistState.Data.Empty
    val Error = WatchlistState.Error(TextRes(string.network_error_no_network))
    val Loading = WatchlistState.Loading
    val NotEmptyWatchList = WatchlistState.Data.NotEmpty(
        nonEmptyListOf(
            WatchlistItemUiModelPreviewData.Inception,
            WatchlistItemUiModelPreviewData.TheWolfOfWallStreet
        )
    )
}

class WatchlistScreenPreviewDataProvider : PreviewParameterProvider<WatchlistState> {

    override val values = sequenceOf(
        WatchlistScreenPreviewData.Loading,
        WatchlistScreenPreviewData.Error,
        WatchlistScreenPreviewData.EmptyWatchlist,
        WatchlistScreenPreviewData.NotEmptyWatchList
    )
}
