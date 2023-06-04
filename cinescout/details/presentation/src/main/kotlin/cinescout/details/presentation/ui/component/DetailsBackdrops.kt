package cinescout.details.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import cinescout.design.theme.Dimens
import cinescout.design.theme.imageBackground
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.FailureImage
import cinescout.resources.R.drawable
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun DetailsBackdrops(urls: ImmutableList<String?>, modifier: Modifier = Modifier) {
    val lazyListState = rememberLazyListState()
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        LazyRow(
            state = lazyListState,
            flingBehavior = rememberSnapperFlingBehavior(lazyListState)
        ) {
            items(urls) { url ->
                Backdrop(modifier = Modifier.fillParentMaxSize(), url = url)
            }
        }
        Row(modifier = Modifier.statusBarsPadding()) {
            val currentIndex by remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
            repeat(urls.size) { index ->
                fun Modifier.background() = when (index) {
                    currentIndex -> background(color = Color.Transparent)
                    else -> background(color = Color.White, shape = CircleShape)
                }

                fun Modifier.border() = when (index) {
                    currentIndex -> border(width = Dimens.Outline, color = Color.White, shape = CircleShape)
                    else -> border(width = Dimens.Outline, color = Color.Black)
                }
                Box(
                    modifier = Modifier
                        .padding(Dimens.Margin.XXSmall)
                        .size(Dimens.Indicator)
                        .background()
                        .border()
                )
            }
        }
    }
}

@Composable
private fun Backdrop(url: String?, modifier: Modifier = Modifier) {
    CoilImage(
        modifier = modifier
            .fillMaxSize()
            .imageBackground(),
        imageModel = { url },
        imageOptions = ImageOptions(contentScale = ContentScale.Crop),
        failure = { FailureImage() },
        loading = { CenteredProgress() },
        previewPlaceholder = drawable.img_backdrop
    )
}
