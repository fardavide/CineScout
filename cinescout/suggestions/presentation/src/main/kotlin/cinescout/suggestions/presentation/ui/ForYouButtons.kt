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
import cinescout.design.theme.CineScoutTheme
import cinescout.movies.domain.sample.TmdbMovieIdSample
import cinescout.screenplay.domain.model.TmdbScreenplayId
import studio.forface.cinescout.design.R

@Composable
internal fun ForYouButtons(itemId: TmdbScreenplayId, actions: ForYouButtons.Actions, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { actions.dislike(itemId) }) {
                Text(text = stringResource(id = R.string.suggestions_for_you_dislike))
            }
            Button(onClick = { actions.like(itemId) }) {
                Text(text = stringResource(id = R.string.suggestions_for_you_like))
            }
        }
        TextButton(onClick = { actions.dislike(itemId) }) {
            Text(text = stringResource(id = R.string.suggestions_for_you_havent_watch))
        }
    }
}

object ForYouButtons {

    data class Actions(
        val dislike: (TmdbScreenplayId) -> Unit,
        val like: (TmdbScreenplayId) -> Unit,
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
    CineScoutTheme {
        ForYouButtons(itemId = TmdbMovieIdSample.Inception, actions = ForYouButtons.Actions.Empty)
    }
}
