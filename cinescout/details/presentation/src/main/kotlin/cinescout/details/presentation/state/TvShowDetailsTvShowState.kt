package cinescout.details.presentation.state

import cinescout.design.TextRes
import cinescout.details.presentation.model.TvShowDetailsUiModel

sealed interface TvShowDetailsTvShowState {

    data class Data(val tvShowDetails: TvShowDetailsUiModel) : TvShowDetailsTvShowState

    data class Error(val message: TextRes) : TvShowDetailsTvShowState

    object Loading : TvShowDetailsTvShowState
}
