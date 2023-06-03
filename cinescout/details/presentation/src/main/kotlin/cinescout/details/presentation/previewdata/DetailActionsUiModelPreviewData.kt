package cinescout.details.presentation.previewdata

import cinescout.design.util.PreviewDataProvider
import cinescout.details.presentation.model.DetailActionsUiModel
import cinescout.details.presentation.sample.DetailActionsUiModelSample

internal class DetailActionsUiModelPreviewData : PreviewDataProvider<DetailActionsUiModel>(
    DetailActionsUiModelSample.AllOn,
    DetailActionsUiModelSample.AllOff
)
