package cinescout.suggestions.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cinescout.design.LocalePreviews
import cinescout.design.R.string
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.suggestions.presentation.model.ForYouType

@Composable
fun ForYouTypeFilter(
    type: ForYouType,
    modifier: Modifier = Modifier,
    onTypeChange: (ForYouType) -> Unit
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(Dimens.Margin.XSmall)) {
        ElevatedFilterChip(
            selected = type == ForYouType.Movies,
            onClick = { onTypeChange(ForYouType.Movies) },
            label = { Text(text = stringResource(id = string.item_type_movies)) }
        )
        ElevatedFilterChip(
            selected = type == ForYouType.TvShows,
            onClick = { onTypeChange(ForYouType.TvShows) },
            label = { Text(text = stringResource(id = string.item_type_tv_shows)) }
        )
    }
}

@Composable
@LocalePreviews.WithBackground
private fun ForYouTypeFilterPreview() {
    CineScoutTheme {
        ForYouTypeFilter(type = ForYouType.Movies, onTypeChange = {})
    }
}
