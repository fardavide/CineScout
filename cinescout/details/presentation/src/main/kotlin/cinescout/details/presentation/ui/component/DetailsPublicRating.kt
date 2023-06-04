package cinescout.details.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.util.NoContentDescription
import cinescout.design.util.PreviewUtils
import cinescout.resources.R.drawable
import cinescout.resources.TextRes
import cinescout.resources.string

@Composable
internal fun DetailsPublicRating(
    average: String,
    count: TextRes,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.Margin.Medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(Dimens.Image.Small),
            painter = painterResource(id = drawable.img_trakt_logo_red_white),
            contentDescription = NoContentDescription
        )
        Text(text = average, style = MaterialTheme.typography.headlineLarge)
        Text(text = string(textRes = count), style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = PreviewUtils.WhiteBackgroundColor)
private fun DetailsPublicRatingPreview() {
    CineScoutTheme {
        DetailsPublicRating(
            average = "8.5",
            count = TextRes("123 votes")
        )
    }
}
