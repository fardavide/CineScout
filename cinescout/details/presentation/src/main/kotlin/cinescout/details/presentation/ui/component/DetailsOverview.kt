package cinescout.details.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import cinescout.details.presentation.sample.ScreenplayDetailsUiModelSample

@Composable
internal fun DetailsOverview(
    tagline: String?,
    overview: String,
    modifier: Modifier = Modifier
) {
    CsCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(vertical = Dimens.Margin.small, horizontal = Dimens.Margin.medium),
            verticalArrangement = Arrangement.spacedBy(Dimens.Margin.medium)
        ) {
            if (tagline != null) {
                Text(
                    text = tagline,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Text(
                text = overview,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
private fun DetailsOverviewPreview() {
    CineScoutTheme {
        DetailsOverview(
            tagline = ScreenplayDetailsUiModelSample.Inception.tagline,
            overview = ScreenplayDetailsUiModelSample.Inception.overview
        )
    }
}

@Preview
@Composable
private fun NoTaglineDetailsOverviewPreview() {
    CineScoutTheme {
        DetailsOverview(
            tagline = null,
            overview = ScreenplayDetailsUiModelSample.Inception.overview
        )
    }
}
