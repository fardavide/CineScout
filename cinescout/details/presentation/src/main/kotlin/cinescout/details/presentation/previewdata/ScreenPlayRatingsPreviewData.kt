package cinescout.details.presentation.previewdata

import cinescout.design.util.PreviewDataProvider
import cinescout.details.presentation.model.ScreenPlayRatingsUiModel
import cinescout.details.presentation.sample.ScreenPlayRatingsUiModelSample

internal class ScreenPlayRatingsPreviewProvider : PreviewDataProvider<ScreenPlayRatingsUiModel>(
    ScreenPlayRatingsUiModelSample.BreakingBad,
    ScreenPlayRatingsUiModelSample.Inception
)
