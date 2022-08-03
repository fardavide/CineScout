package cinescout.design.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import cinescout.design.TextRes
import cinescout.design.string
import cinescout.design.theme.Dimens

@Composable
fun CenteredErrorText(text: TextRes, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize().padding(Dimens.Margin.Medium), contentAlignment = Alignment.Center) {
        Text(
            text = string(textRes = text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}
