package cinescout.details.presentation.state

import cinescout.details.presentation.model.ScreenplayDetailsUiModel
import cinescout.resources.TextRes

sealed interface ScreenplayDetailsItemState {

    data class Data(val uiModel: ScreenplayDetailsUiModel) : ScreenplayDetailsItemState

    data class Error(val message: TextRes) : ScreenplayDetailsItemState

    object Loading : ScreenplayDetailsItemState
}
