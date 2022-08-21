package cinescout.design.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.TextRes
import cinescout.design.string
import cinescout.design.testdata.MessageTextResTestData
import cinescout.design.theme.Dimens
import cinescout.design.util.NoContentDescription
import studio.forface.cinescout.design.R.drawable

@Composable
fun CenteredErrorText(text: TextRes, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.Margin.Medium),
        contentAlignment = Alignment.Center
    ) {
        ErrorText(text = text)
    }
}

@Composable
fun ErrorScreen(text: TextRes, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.Margin.Medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.Margin.XLarge, alignment = Alignment.CenterVertically)
    ) {
        BoxWithConstraints {
            Image(
                modifier = Modifier
                    .size(minOf(maxWidth / 2, maxHeight / 3))
                    .aspectRatio(1f),
                painter = painterResource(id = drawable.img_error),
                contentDescription = NoContentDescription
            )
        }
        ErrorText(text = text)
    }
}

@Composable
fun ErrorText(text: TextRes, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = string(textRes = text),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
@Preview(showSystemUi = true)
@Preview(showSystemUi = true, device = Devices.TABLET)
private fun ErrorScreenPreview() {
    ErrorScreen(text = MessageTextResTestData.NoNetworkError)
}
