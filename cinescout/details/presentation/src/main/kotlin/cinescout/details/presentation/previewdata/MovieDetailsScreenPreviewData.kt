package cinescout.details.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import cinescout.design.TextRes
import cinescout.details.presentation.model.MovieDetailsState
import studio.forface.cinescout.design.R.string

object MovieDetailsScreenPreviewData {

    val Error = MovieDetailsState.Error(TextRes(string.network_error_no_network))
    val Loading = MovieDetailsState.Loading
    val Inception = MovieDetailsState.Data(
        MovieDetailsUiModelPreviewData.Inception
    )
    val TheWolfOfWallStreet = MovieDetailsState.Data(
        MovieDetailsUiModelPreviewData.TheWolfOfWallStreet
    )
}

class MovieDetailsScreenPreviewDataProvider : PreviewParameterProvider<MovieDetailsState> {

    override val values = sequenceOf(
        MovieDetailsScreenPreviewData.Loading,
        MovieDetailsScreenPreviewData.Error,
        MovieDetailsScreenPreviewData.Inception,
        MovieDetailsScreenPreviewData.TheWolfOfWallStreet
    )
}
