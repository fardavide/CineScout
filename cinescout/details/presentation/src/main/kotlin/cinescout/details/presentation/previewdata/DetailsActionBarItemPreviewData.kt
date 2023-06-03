package cinescout.details.presentation.previewdata

import cinescout.design.util.PreviewDataProvider
import cinescout.details.presentation.model.DetailsActionItemUiModel
import cinescout.details.presentation.sample.DetailsActionItemUiModelSample

internal class DetailsActionItemUiModelPreviewProvider : PreviewDataProvider<DetailsActionItemUiModel>(
    DetailsActionItemUiModelSample.InHistory,
    DetailsActionItemUiModelSample.NotInHistory,
    DetailsActionItemUiModelSample.HasPersonalRating,
    DetailsActionItemUiModelSample.NoPersonalRating,
    DetailsActionItemUiModelSample.InWatchlist,
    DetailsActionItemUiModelSample.NotInWatchlist
)
