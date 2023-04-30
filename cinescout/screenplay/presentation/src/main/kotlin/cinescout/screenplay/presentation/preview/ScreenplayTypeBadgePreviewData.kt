package cinescout.screenplay.presentation.preview

import cinescout.design.util.PreviewDataProvider
import cinescout.screenplay.domain.model.ScreenplayType

internal class ScreenplayTypeBadgePreviewData : PreviewDataProvider<ScreenplayType>(
    ScreenplayType.Movie,
    ScreenplayType.TvShow
)
