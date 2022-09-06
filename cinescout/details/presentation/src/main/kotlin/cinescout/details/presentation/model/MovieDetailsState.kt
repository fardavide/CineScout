package cinescout.details.presentation.model

import cinescout.design.TextRes

sealed interface MovieDetailsState {

    data class Data(val movieDetails: MovieDetailsUiModel) : MovieDetailsState

    data class Error(val message: TextRes) : MovieDetailsState

    object Loading : MovieDetailsState
}
