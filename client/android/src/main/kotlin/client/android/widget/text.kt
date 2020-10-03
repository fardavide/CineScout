package client.android.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.currentTextStyle
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import client.resource.Strings
import client.resource.StringResources
import studio.forface.cinescout.R

/**
 * A [Text] widget that fill the max width and align the text in the center
 */
@Composable
fun CenteredText(modifier: Modifier = Modifier, text: String, style: TextStyle = currentTextStyle()) {
    Text(modifier = modifier.fillMaxWidth(), textAlign = TextAlign.Center, style = style, text = text)
}
