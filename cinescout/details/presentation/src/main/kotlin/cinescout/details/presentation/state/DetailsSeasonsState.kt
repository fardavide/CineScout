package cinescout.details.presentation.state

import cinescout.details.presentation.model.DetailsSeasonsUiModel
import cinescout.resources.TextRes
import cinescout.screenplay.domain.model.Movie

sealed interface DetailsSeasonsState {

    data class Data(val uiModel: DetailsSeasonsUiModel) : DetailsSeasonsState

    data class Error(val message: TextRes) : DetailsSeasonsState

    object Loading : DetailsSeasonsState

    /**
     * Screenplay has no seasons.
     * @see Movie
     */
    object NoSeasons : DetailsSeasonsState
}
