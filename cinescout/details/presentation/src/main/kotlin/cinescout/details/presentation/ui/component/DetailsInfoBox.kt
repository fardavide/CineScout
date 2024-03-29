package cinescout.details.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.CsCard
import cinescout.details.presentation.model.ScreenplayDetailsUiModel
import cinescout.details.presentation.sample.ScreenplayDetailsUiModelSample
import cinescout.resources.TextRes
import cinescout.resources.string

@Composable
internal fun DetailsInfoBox(
    title: String,
    premiere: ScreenplayDetailsUiModel.Premiere,
    runtime: TextRes?,
    modifier: Modifier = Modifier
) {
    CsCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.Margin.small, horizontal = Dimens.Margin.medium)
        ) {
            Text(text = title, maxLines = 2, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.padding(Dimens.Margin.small))
            Text(text = premiere.string(), style = MaterialTheme.typography.labelLarge)
            if (runtime != null) {
                Text(text = string(textRes = runtime), style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Preview
@Composable
private fun DetailsInfoBoxPreview() {
    CineScoutTheme {
        DetailsInfoBox(
            title = ScreenplayDetailsUiModelSample.Inception.title,
            premiere = ScreenplayDetailsUiModelSample.Inception.premiere,
            runtime = ScreenplayDetailsUiModelSample.Inception.runtime
        )
    }
}
