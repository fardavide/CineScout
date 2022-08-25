package cinescout.lists.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import arrow.core.nonEmptyListOf
import cinescout.design.TextRes
import cinescout.lists.presentation.model.WatchlistItemUiModel
import cinescout.lists.presentation.model.WatchlistState
import cinescout.movies.domain.testdata.MovieTestData
import studio.forface.cinescout.design.R.string

object WatchlistScreenPreviewData {

    val EmptyWatchlist = WatchlistState.Data.Empty
    val Error = WatchlistState.Error(TextRes(string.network_error_no_network))
    val Loading = WatchlistState.Loading
    val NotEmptyWatchList = WatchlistState.Data.NotEmpty(
        nonEmptyListOf(
            WatchlistItemUiModel(
                tmdbId = MovieTestData.Inception.tmdbId,
                title = MovieTestData.Inception.title
            ),
            WatchlistItemUiModel(
                tmdbId = MovieTestData.TheWolfOfWallStreet.tmdbId,
                title = MovieTestData.TheWolfOfWallStreet.title
            )
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
