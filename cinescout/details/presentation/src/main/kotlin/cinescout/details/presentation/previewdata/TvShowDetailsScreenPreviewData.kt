package cinescout.details.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import cinescout.design.TextRes
import cinescout.details.presentation.model.TvShowDetailsState
import studio.forface.cinescout.design.R.string

object TvShowDetailsScreenPreviewData {

    val Error = TvShowDetailsState.Error(TextRes(string.network_error_no_network))
    val Loading = TvShowDetailsState.Loading
    val BreakingBad = TvShowDetailsState.Data(
        TvShowDetailsUiModelPreviewData.BreakingBad
    )
    val Grimm = TvShowDetailsState.Data(
        TvShowDetailsUiModelPreviewData.Grimm
    )
}

class TvShowDetailsScreenPreviewDataProvider : PreviewParameterProvider<TvShowDetailsState> {

    override val values = sequenceOf(
        TvShowDetailsScreenPreviewData.Loading,
        TvShowDetailsScreenPreviewData.Error,
        TvShowDetailsScreenPreviewData.Grimm,
        TvShowDetailsScreenPreviewData.BreakingBad
    )
}
