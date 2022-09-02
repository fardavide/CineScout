package cinescout.lists.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import arrow.core.nonEmptyListOf
import cinescout.design.TextRes
import cinescout.lists.presentation.model.RatedListState
import studio.forface.cinescout.design.R.string

object RatedListScreenPreviewData {

    val EmptyWatchlist = RatedListState.Data.Empty
    val Error = RatedListState.Error(TextRes(string.network_error_no_network))
    val Loading = RatedListState.Loading
    val NotEmptyWatchList = RatedListState.Data.NotEmpty(
        nonEmptyListOf(
            ListItemUiModelPreviewData.Inception,
            ListItemUiModelPreviewData.TheWolfOfWallStreet
        )
    )
}

class RatedListScreenPreviewDataProvider : PreviewParameterProvider<RatedListState> {

    override val values = sequenceOf(
        RatedListScreenPreviewData.Loading,
        RatedListScreenPreviewData.Error,
        RatedListScreenPreviewData.EmptyWatchlist,
        RatedListScreenPreviewData.NotEmptyWatchList
    )
}
