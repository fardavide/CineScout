package cinescout.suggestions.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cinescout.design.LocalePreviews
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.CsFilterChip
import cinescout.resources.R.string
import cinescout.suggestions.presentation.model.ForYouType

@Composable
internal fun ForYouTypeFilter(
    type: ForYouType,
    modifier: Modifier = Modifier,
    onTypeChange: (ForYouType) -> Unit
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(Dimens.Margin.xSmall)) {
        CsFilterChip(
            selected = type == ForYouType.Movies,
            onClick = { onTypeChange(ForYouType.Movies) },
            label = { Text(text = stringResource(id = string.item_type_movies)) }
        )
        CsFilterChip(
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
