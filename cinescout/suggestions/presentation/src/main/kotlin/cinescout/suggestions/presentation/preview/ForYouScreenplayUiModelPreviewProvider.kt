package cinescout.suggestions.presentation.preview

import cinescout.design.util.PreviewDataProvider
import cinescout.suggestions.presentation.model.ForYouScreenplayUiModel
import cinescout.suggestions.presentation.sample.ForYouMovieUiModelSample

class ForYouScreenplayUiModelPreviewProvider : PreviewDataProvider<ForYouScreenplayUiModel>(
    ForYouMovieUiModelSample.Inception,
    ForYouMovieUiModelSample.War
)
