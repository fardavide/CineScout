package cinescout.details.presentation.state

import cinescout.design.TextRes
import cinescout.details.presentation.model.MovieDetailsUiModel

sealed interface MovieDetailsMovieState {

    data class Data(val movieDetails: MovieDetailsUiModel) : MovieDetailsMovieState

    data class Error(val message: TextRes) : MovieDetailsMovieState

    object Loading : MovieDetailsMovieState
}
