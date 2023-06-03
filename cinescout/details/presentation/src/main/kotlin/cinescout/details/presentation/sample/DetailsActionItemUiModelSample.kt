package cinescout.details.presentation.sample

import arrow.core.none
import arrow.core.some
import cinescout.details.presentation.model.DetailsActionItemUiModel
import cinescout.resources.ImageRes
import cinescout.resources.R.drawable
import cinescout.resources.TextRes

internal object DetailsActionItemUiModelSample {

    val InHistory = DetailsActionItemUiModel(
        badgeResource = ImageRes(drawable.ic_check_round_color).some(),
        contentDescription = TextRes("Add another to history"),
        imageRes = ImageRes(drawable.ic_clock)
    )
    val NotInHistory = DetailsActionItemUiModel(
        badgeResource = none(),
        contentDescription = TextRes("Add to history"),
        imageRes = ImageRes(drawable.ic_clock)
    )

    val HasPersonalRating = DetailsActionItemUiModel(
        badgeResource = TextRes("8").some(),
        contentDescription = TextRes("Change personal rating"),
        imageRes = ImageRes(drawable.ic_star_filled)
    )
    val NoPersonalRating = DetailsActionItemUiModel(
        badgeResource = none(),
        contentDescription = TextRes("Add personal rating"),
        imageRes = ImageRes(drawable.ic_star)
    )

    val InWatchlist = DetailsActionItemUiModel(
        badgeResource = none(),
        contentDescription = TextRes("Remove from watchlist"),
        imageRes = ImageRes(drawable.ic_bookmark_filled)
    )
    val NotInWatchlist = DetailsActionItemUiModel(
        badgeResource = none(),
        contentDescription = TextRes("Add to watchlist"),
        imageRes = ImageRes(drawable.ic_bookmark)
    )
}
