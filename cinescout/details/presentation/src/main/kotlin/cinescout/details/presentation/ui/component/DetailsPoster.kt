package cinescout.details.presentation.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import cinescout.design.theme.imageBackground
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.FailureImage
import cinescout.resources.R.drawable
import com.skydoves.landscapist.coil.CoilImage

@Composable
internal fun DetailsPoster(url: String?, modifier: Modifier = Modifier) {
    CoilImage(
        modifier = modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.medium)
            .imageBackground(),
        imageModel = { url },
        failure = { FailureImage() },
        loading = { CenteredProgress() },
        previewPlaceholder = drawable.img_poster
    )
}
