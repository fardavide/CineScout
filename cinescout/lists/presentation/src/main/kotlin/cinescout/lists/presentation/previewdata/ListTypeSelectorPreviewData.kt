package cinescout.lists.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import cinescout.lists.domain.ListType

object ListTypeSelectorPreviewData {

    val Movies = ListType.Movies
    val All = ListType.All
    val TvShows = ListType.TvShows
}

internal class ListTypeSelectorPreviewProvider : PreviewParameterProvider<ListType> {

    override val values = sequenceOf(
        ListTypeSelectorPreviewData.Movies,
        ListTypeSelectorPreviewData.All,
        ListTypeSelectorPreviewData.TvShows
    )
}
