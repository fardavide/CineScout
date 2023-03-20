package cinescout.design.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cinescout.design.theme.CineScoutTheme
import cinescout.design.util.NoContentDescription
import cinescout.resources.R.drawable
import com.skydoves.landscapist.coil.CoilImage

/**
 * @see CoilImage `failure` parameter
 */
@Composable
fun FailureImage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = drawable.ic_warning_30),
            contentDescription = NoContentDescription
        )
    }
}

@Preview
@Composable
private fun FailureImagePreview() {
    CineScoutTheme {
        Box(modifier = Modifier.size(300.dp)) {
            FailureImage()
        }
    }
}
