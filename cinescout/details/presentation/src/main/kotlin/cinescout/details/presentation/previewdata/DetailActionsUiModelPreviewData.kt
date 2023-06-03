package cinescout.details.presentation.previewdata

import cinescout.design.util.PreviewDataProvider
import cinescout.details.presentation.model.DetailsActionsUiModel
import cinescout.details.presentation.sample.DetailActionsUiModelSample

internal class DetailActionsUiModelPreviewData : PreviewDataProvider<DetailsActionsUiModel>(
    DetailActionsUiModelSample.AllOn,
    DetailActionsUiModelSample.AllOff
)
