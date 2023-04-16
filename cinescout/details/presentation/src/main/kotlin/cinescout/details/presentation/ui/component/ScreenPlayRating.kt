package cinescout.details.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import cinescout.design.AdaptivePreviews
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.util.NoContentDescription
import cinescout.details.presentation.model.ScreenplayRatingsUiModel
import cinescout.details.presentation.previewdata.ScreenPlayRatingsPreviewProvider
import cinescout.resources.R.drawable
import cinescout.resources.R.string

@Composable
internal fun ScreenplayRatings(ratings: ScreenplayRatingsUiModel, openRateDialog: () -> Unit) {
    Row(
        modifier = Modifier.padding(horizontal = Dimens.Margin.Medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .border(
                    width = Dimens.Outline,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = MaterialTheme.shapes.small
                )
                .padding(Dimens.Margin.Small)
                .size(Dimens.Icon.Medium),
            painter = painterResource(id = drawable.img_trakt_logo_red_white),
            contentDescription = stringResource(id = string.tmdb_logo_description)
        )
        Spacer(modifier = Modifier.width(Dimens.Margin.Small))
        Column {
            Text(text = ratings.publicAverage, style = MaterialTheme.typography.titleMedium)
            Text(text = ratings.publicCount, style = MaterialTheme.typography.bodySmall)
        }
    }
    ScreenPlayPersonalRating(rating = ratings.personal, openRateDialog = openRateDialog)
}

@Composable
private fun ScreenPlayPersonalRating(rating: ScreenplayRatingsUiModel.Personal, openRateDialog: () -> Unit) {
    FilledTonalButton(
        onClick = openRateDialog,
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(horizontal = Dimens.Margin.Medium, vertical = Dimens.Margin.Small)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            when (rating) {
                ScreenplayRatingsUiModel.Personal.NotRated -> {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = NoContentDescription)
                    Text(text = stringResource(id = string.details_rate_now))
                }
                is ScreenplayRatingsUiModel.Personal.Rated -> Text(
                    text = rating.stringValue,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
@AdaptivePreviews.Plain
private fun ScreenPlayRatingsPreview(
    @PreviewParameter(ScreenPlayRatingsPreviewProvider::class) ratings: ScreenplayRatingsUiModel
) {
    CineScoutTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.Margin.Medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScreenplayRatings(
                ratings = ratings,
                openRateDialog = {}
            )
        }
    }
}
