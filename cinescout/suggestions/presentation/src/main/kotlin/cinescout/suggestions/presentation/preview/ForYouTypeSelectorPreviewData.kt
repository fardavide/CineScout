package cinescout.suggestions.presentation.preview

import cinescout.design.util.PreviewDataProvider
import cinescout.suggestions.presentation.model.ForYouType

internal class ForYouTypePreviewProvider : PreviewDataProvider<ForYouType>(
    ForYouType.Movies,
    ForYouType.TvShows
)
