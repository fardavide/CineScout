package cinescout.suggestions.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cinescout.design.PlainAdaptivePreviews
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.resources.R.drawable
import cinescout.resources.R.string
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.sample.ScreenplayIdsSample

@Composable
internal fun ForYouButtons(
    itemId: ScreenplayIds,
    actions: ForYouButtons.Actions,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(Dimens.Margin.medium)) {
        TextButton(onClick = { actions.dislike(itemId) }) {
            Text(text = stringResource(id = string.suggestions_for_you_havent_watch))
        }
        IconButton(onClick = { actions.dislike(itemId) }) {
            Icon(
                modifier = Modifier.rotate(180f),
                painter = painterResource(id = drawable.ic_like),
                contentDescription = stringResource(id = string.suggestions_for_you_dislike)
            )
        }
        IconButton(onClick = { actions.like(itemId) }) {
            Icon(
                painter = painterResource(id = drawable.ic_like),
                contentDescription = stringResource(id = string.suggestions_for_you_like)
            )
        }
    }
}

object ForYouButtons {

    data class Actions(
        val dislike: (ScreenplayIds) -> Unit,
        val like: (ScreenplayIds) -> Unit
    ) {

        companion object {

            val Empty = Actions(
                dislike = {},
                like = {}
            )
        }
    }
}

@Composable
@PlainAdaptivePreviews
private fun ForYouButtonsPreview() {
    CineScoutTheme {
        ForYouButtons(
            itemId = ScreenplayIdsSample.Inception,
            actions = ForYouButtons.Actions.Empty
        )
    }
}
