package cinescout.details.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.util.NoContentDescription
import cinescout.resources.R.drawable

@Composable
internal fun DetailsPublicRating() {
    Row {
        Image(
            modifier = Modifier.size(Dimens.Image.Small),
            painter = painterResource(id = drawable.img_trakt_logo_red_white),
            contentDescription = NoContentDescription
        )
    }
}

@Preview
@Composable
private fun DetailsPublicRatingPreview() {
    CineScoutTheme {
        DetailsPublicRating()
    }
}
