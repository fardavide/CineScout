package cinescout.details.presentation.model

import cinescout.design.TextRes

sealed interface TvShowDetailsState {

    data class Data(val tvShowDetails: TvShowDetailsUiModel) : TvShowDetailsState

    data class Error(val message: TextRes) : TvShowDetailsState

    object Loading : TvShowDetailsState
}
