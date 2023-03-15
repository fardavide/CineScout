package cinescout.suggestions.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cinescout.design.AdaptivePreviews
import cinescout.design.R.string
import cinescout.design.theme.CineScoutTheme
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import cinescout.utils.compose.Adaptive

@Composable
internal fun ForYouButtons(
    mode: ForYouScreen.Mode,
    itemId: TmdbScreenplayId,
    actions: ForYouButtons.Actions,
    modifier: Modifier = Modifier
) {
    when (mode) {
        ForYouScreen.Mode.Horizontal -> Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { actions.dislike(itemId) }) {
                Text(text = stringResource(id = string.suggestions_for_you_dislike))
            }
            TextButton(onClick = { actions.dislike(itemId) }) {
                Text(text = stringResource(id = string.suggestions_for_you_havent_watch))
            }
            Button(onClick = { actions.like(itemId) }) {
                Text(text = stringResource(id = string.suggestions_for_you_like))
            }
        }

        is ForYouScreen.Mode.Vertical -> Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { actions.dislike(itemId) }) {
                Text(text = stringResource(id = string.suggestions_for_you_dislike))
            }
            TextButton(onClick = { actions.dislike(itemId) }) {
                Text(text = stringResource(id = string.suggestions_for_you_havent_watch))
            }
            Button(onClick = { actions.like(itemId) }) {
                Text(text = stringResource(id = string.suggestions_for_you_like))
            }
        }
    }
}

object ForYouButtons {

    data class Actions(
        val dislike: (TmdbScreenplayId) -> Unit,
        val like: (TmdbScreenplayId) -> Unit
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
@AdaptivePreviews.Plain
private fun ForYouButtonsPreview() {
    Adaptive { windowSizeClass ->
        val mode = ForYouScreen.Mode.forClass(windowSizeClass)
        CineScoutTheme {
            ForYouButtons(
                mode = mode,
                itemId = TmdbScreenplayIdSample.Inception,
                actions = ForYouButtons.Actions.Empty
            )
        }
    }
}
