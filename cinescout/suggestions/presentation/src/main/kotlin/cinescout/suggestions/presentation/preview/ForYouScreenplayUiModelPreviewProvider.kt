package cinescout.suggestions.presentation.preview

import cinescout.design.util.PreviewDataProvider
import cinescout.suggestions.presentation.model.ForYouScreenplayUiModel
import cinescout.suggestions.presentation.sample.ForYouScreenplayUiModelSample

class ForYouScreenplayUiModelPreviewProvider : PreviewDataProvider<ForYouScreenplayUiModel>(
    ForYouScreenplayUiModelSample.Inception,
    ForYouScreenplayUiModelSample.War
)
