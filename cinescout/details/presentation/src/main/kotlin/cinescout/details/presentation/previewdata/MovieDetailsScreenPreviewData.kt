package cinescout.details.presentation.previewdata

import cinescout.design.TextRes
import cinescout.design.model.ConnectionStatusUiModel
import cinescout.design.util.PreviewDataProvider
import cinescout.details.presentation.sample.MovieDetailsUiModelSample
import cinescout.details.presentation.state.MovieDetailsMovieState
import cinescout.details.presentation.state.MovieDetailsState
import studio.forface.cinescout.design.R.string

object MovieDetailsScreenPreviewData {

    val Inception = MovieDetailsState(
        movieState = MovieDetailsMovieState.Data(
            MovieDetailsUiModelSample.Inception
        ),
        connectionStatus = ConnectionStatusUiModel.AllConnected
    )
    val Loading = MovieDetailsState.Loading
    val TheWolfOfWallStreet = MovieDetailsState(
        movieState = MovieDetailsMovieState.Data(
            MovieDetailsUiModelSample.TheWolfOfWallStreet
        ),
        connectionStatus = ConnectionStatusUiModel.AllConnected
    )
    val TmdbOffline = MovieDetailsState(
        movieState = MovieDetailsMovieState.Error(TextRes(string.network_error_no_network)),
        connectionStatus = ConnectionStatusUiModel.TmdbOffline
    )
}

class MovieDetailsScreenPreviewDataProvider : PreviewDataProvider<MovieDetailsState>(
    MovieDetailsScreenPreviewData.Inception,
    MovieDetailsScreenPreviewData.TheWolfOfWallStreet,
    MovieDetailsScreenPreviewData.TmdbOffline,
    MovieDetailsScreenPreviewData.Loading
)
