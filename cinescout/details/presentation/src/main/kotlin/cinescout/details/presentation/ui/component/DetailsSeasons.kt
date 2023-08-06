package cinescout.details.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.details.presentation.model.DetailsSeasonsUiModel
import cinescout.details.presentation.previewdata.DetailsSeasonsUiModelPreviewData
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.resources.string

@Composable
internal fun DetailsSeasons(
    uiModel: DetailsSeasonsUiModel,
    openSeasons: (seasons: DetailsSeasonsUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    DetailsNavigableRow(
        modifier = modifier,
        onClick = { openSeasons(uiModel) },
        contentDescription = TextRes(string.details_see_seasons)
    ) {
        Column(
            modifier = Modifier.height(IntrinsicSize.Max),
            verticalArrangement = Arrangement.spacedBy(Dimens.Margin.small)
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(Dimens.Margin.medium),
                verticalArrangement = Arrangement.spacedBy(Dimens.Margin.small)
            ) {
                Text(
                    text = string(textRes = uiModel.totalSeasons),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    modifier = Modifier.alpha(0.32f),
                    text = string(textRes = uiModel.watchedSeasons),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), progress = uiModel.progress)
        }
    }
}

@Preview
@Composable
private fun DetailsSeasonsPreview(
    @PreviewParameter(DetailsSeasonsUiModelPreviewData::class) uiModel: DetailsSeasonsUiModel
) {
    CineScoutTheme {
        DetailsSeasons(uiModel = uiModel, openSeasons = {})
    }
}
