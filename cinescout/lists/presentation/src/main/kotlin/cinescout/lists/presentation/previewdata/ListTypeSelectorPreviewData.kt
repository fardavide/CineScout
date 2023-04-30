package cinescout.lists.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import cinescout.screenplay.domain.model.ScreenplayTypeFilter

object ListTypeSelectorPreviewData {

    val Movies = ScreenplayTypeFilter.Movies
    val All = ScreenplayTypeFilter.All
    val TvShows = ScreenplayTypeFilter.TvShows
}

internal class ListTypeSelectorPreviewProvider : PreviewParameterProvider<ScreenplayTypeFilter> {

    override val values = sequenceOf(
        ListTypeSelectorPreviewData.Movies,
        ListTypeSelectorPreviewData.All,
        ListTypeSelectorPreviewData.TvShows
    )
}
