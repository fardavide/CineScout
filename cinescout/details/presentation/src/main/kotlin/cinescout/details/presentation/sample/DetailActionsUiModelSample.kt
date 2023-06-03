package cinescout.details.presentation.sample

import cinescout.details.presentation.model.DetailActionsUiModel

internal object DetailActionsUiModelSample {

    val AllOn = DetailActionsUiModel(
        historyUiModel = DetailsActionItemUiModelSample.InHistory,
        personalRatingUiModel = DetailsActionItemUiModelSample.HasPersonalRating,
        watchlistUiModel = DetailsActionItemUiModelSample.InWatchlist
    )

    val AllOff = DetailActionsUiModel(
        historyUiModel = DetailsActionItemUiModelSample.NotInHistory,
        personalRatingUiModel = DetailsActionItemUiModelSample.NoPersonalRating,
        watchlistUiModel = DetailsActionItemUiModelSample.NotInWatchlist
    )
}
