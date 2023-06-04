package cinescout.details.presentation.ui.component

import androidx.compose.foundation.background
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
import cinescout.details.presentation.sample.ScreenplayDetailsUiModelSample
import cinescout.resources.TextRes
import cinescout.resources.string

@Composable
internal fun DetailsInfoBox(
    title: String,
    releaseDate: String,
    runtime: TextRes?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                shape = MaterialTheme.shapes.medium
            )
            .padding(vertical = Dimens.Margin.Small, horizontal = Dimens.Margin.Medium)
    ) {
        Text(text = title, maxLines = 2, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.padding(Dimens.Margin.Small))
        Text(text = releaseDate, style = MaterialTheme.typography.labelLarge)
        if (runtime != null) {
            Text(text = string(textRes = runtime), style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Preview
@Composable
private fun DetailsInfoBoxPreview() {
    CineScoutTheme {
        DetailsInfoBox(
            title = ScreenplayDetailsUiModelSample.Inception.title,
            releaseDate = ScreenplayDetailsUiModelSample.Inception.releaseDate,
            runtime = ScreenplayDetailsUiModelSample.Inception.runtime
        )
    }
}
