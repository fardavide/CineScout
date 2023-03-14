package cinescout.details.presentation.state

import cinescout.design.TextRes
import cinescout.details.presentation.model.ScreenplayDetailsUiModel

sealed interface ScreenplayDetailsItemState {

    data class Data(val uiModel: ScreenplayDetailsUiModel) : ScreenplayDetailsItemState

    data class Error(val message: TextRes) : ScreenplayDetailsItemState

    object Loading : ScreenplayDetailsItemState
}
