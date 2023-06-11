package cinescout.details.presentation.model

import arrow.core.Option
import cinescout.resources.ImageRes
import cinescout.resources.Resource
import cinescout.resources.TextRes

internal data class DetailsActionsUiModel(
    val actionItemUiModel: DetailsActionItemUiModel,
    val personalRatingUiModel: DetailsActionItemUiModel,
    val watchlistUiModel: DetailsActionItemUiModel
)

data class DetailsActionItemUiModel(
    val badgeResource: Option<Resource>,
    val contentDescription: TextRes,
    val imageRes: ImageRes
)
