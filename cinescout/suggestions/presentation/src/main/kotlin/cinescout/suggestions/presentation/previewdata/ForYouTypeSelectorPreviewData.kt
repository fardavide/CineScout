package cinescout.suggestions.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import cinescout.suggestions.presentation.model.ForYouType

object ForYouTypeSelectorPreviewData {

    val Movies = ForYouType.Movies
    val TvShows = ForYouType.TvShows
}

internal class ForYouTypeSelectorPreviewProvider : PreviewParameterProvider<ForYouType> {

    override val values = sequenceOf(
        ForYouTypeSelectorPreviewData.Movies,
        ForYouTypeSelectorPreviewData.TvShows
    )
}
