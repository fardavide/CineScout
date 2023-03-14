package cinescout.lists.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import cinescout.screenplay.domain.model.ScreenplayType

object ListTypeSelectorPreviewData {

    val Movies = ScreenplayType.Movies
    val All = ScreenplayType.All
    val TvShows = ScreenplayType.TvShows
}

internal class ListTypeSelectorPreviewProvider : PreviewParameterProvider<ScreenplayType> {

    override val values = sequenceOf(
        ListTypeSelectorPreviewData.Movies,
        ListTypeSelectorPreviewData.All,
        ListTypeSelectorPreviewData.TvShows
    )
}
