package cinescout.details.presentation.sample

import cinescout.details.presentation.model.DetailsActionsUiModel

internal object DetailActionsUiModelSample {

    val AllOn = DetailsActionsUiModel(
        historyUiModel = DetailsActionItemUiModelSample.InHistory,
        personalRatingUiModel = DetailsActionItemUiModelSample.HasPersonalRating,
        watchlistUiModel = DetailsActionItemUiModelSample.InWatchlist
    )

    val AllOff = DetailsActionsUiModel(
        historyUiModel = DetailsActionItemUiModelSample.NotInHistory,
        personalRatingUiModel = DetailsActionItemUiModelSample.NoPersonalRating,
        watchlistUiModel = DetailsActionItemUiModelSample.NotInWatchlist
    )
}
