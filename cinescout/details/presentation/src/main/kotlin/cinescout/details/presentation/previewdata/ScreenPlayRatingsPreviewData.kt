package cinescout.details.presentation.previewdata

import cinescout.design.util.PreviewDataProvider
import cinescout.details.presentation.model.ScreenplayRatingsUiModel
import cinescout.details.presentation.sample.ScreenPlayRatingsUiModelSample

internal class ScreenPlayRatingsPreviewProvider : PreviewDataProvider<ScreenplayRatingsUiModel>(
    ScreenPlayRatingsUiModelSample.BreakingBad,
    ScreenPlayRatingsUiModelSample.Inception
)
