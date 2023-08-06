package cinescout.screenplay.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.resources.R.string
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.presentation.preview.ScreenplayTypeBadgePreviewData

@Composable
fun ScreenplayTypeBadge(type: ScreenplayType, modifier: Modifier = Modifier) {
    val textRes = when (type) {
        ScreenplayType.Movie -> string.item_type_movie
        ScreenplayType.TvShow -> string.item_type_tv_show
    }
    Text(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                shape = MaterialTheme.shapes.extraSmall
            )
            .padding(Dimens.Margin.xSmall),
        text = stringResource(id = textRes),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.inverseOnSurface
    )
}

@Preview
@Composable
private fun ScreenplayTypeBadgePreview(
    @PreviewParameter(ScreenplayTypeBadgePreviewData::class) type: ScreenplayType
) {
    CineScoutTheme {
        ScreenplayTypeBadge(type = type)
    }
}
