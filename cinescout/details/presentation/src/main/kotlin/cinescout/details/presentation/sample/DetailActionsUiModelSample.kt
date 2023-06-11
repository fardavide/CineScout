package cinescout.details.presentation.sample

import cinescout.details.presentation.model.DetailsActionsUiModel

internal object DetailActionsUiModelSample {

    val AllOn = DetailsActionsUiModel(
        actionItemUiModel = DetailsActionItemUiModelSample.InHistory,
        personalRatingUiModel = DetailsActionItemUiModelSample.HasPersonalRating,
        watchlistUiModel = DetailsActionItemUiModelSample.InWatchlist
    )

    val AllOff = DetailsActionsUiModel(
        actionItemUiModel = DetailsActionItemUiModelSample.NotInHistory,
        personalRatingUiModel = DetailsActionItemUiModelSample.NoPersonalRating,
        watchlistUiModel = DetailsActionItemUiModelSample.NotInWatchlist
    )
}
