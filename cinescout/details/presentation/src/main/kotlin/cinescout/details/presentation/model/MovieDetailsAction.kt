package cinescout.details.presentation.model

import cinescout.movies.domain.model.Rating

sealed interface MovieDetailsAction {

    data class RateMovie(val rating: Rating) : MovieDetailsAction
}
