package client.android.widget

import androidx.compose.foundation.Image
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.vectorResource
import client.resource.StringResources
import client.resource.Strings
import studio.forface.cinescout.R

/**
 * Show an error icon with a message, using the given [message] or [StringResources.GenericError]
 */
@Composable
fun ErrorMessage(message: String? = null) {
    Image(asset = vectorResource(id = R.drawable.ic_problem_color))
    CenteredText(text = message ?: Strings.GenericError, style = MaterialTheme.typography.h4)
}

/**
 * Show a loading message, using [StringResources.LoadingMessage]
 */
@Composable
fun LoadingMessage() {
    CenteredText(text = Strings.LoadingMessage, style = MaterialTheme.typography.h4)
}
