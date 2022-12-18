package cinescout.details.presentation.state

import cinescout.design.model.ConnectionStatusUiModel

data class MovieDetailsState(
    val movieState: MovieDetailsMovieState,
    val connectionStatus: ConnectionStatusUiModel
) {

    companion object {

        val Loading = MovieDetailsState(
            movieState = MovieDetailsMovieState.Loading,
            connectionStatus = ConnectionStatusUiModel.AllConnected
        )
    }
}
