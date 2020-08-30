package client.android.widget

import androidx.compose.foundation.Text
import androidx.compose.foundation.currentTextStyle
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

/**
 * A [Text] widget that fill the max width and align the text in the center
 */
@Composable
fun CenteredText(text: String, style: TextStyle = currentTextStyle()) {
    Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, style = style, text = text)
}
