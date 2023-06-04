package cinescout.details.presentation.ui.component

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import cinescout.design.theme.Dimens
import cinescout.design.theme.imageBackground
import cinescout.details.presentation.model.ScreenplayDetailsUiModel
import cinescout.resources.R.drawable
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun DetailsVideos(
    videos: ImmutableList<ScreenplayDetailsUiModel.Video>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        LazyRow {
            items(videos) { video ->
                Column(
                    modifier = Modifier
                        .width(maxWidth * 47 / 100)
                        .padding(horizontal = Dimens.Margin.XSmall)
                        .clickable {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(video.url)
                                )
                            )
                        }
                ) {
                    CoilImage(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .imageBackground(),
                        imageModel = { video.previewUrl },
                        imageOptions = ImageOptions(contentDescription = video.title),
                        previewPlaceholder = drawable.img_video
                    )
                    Spacer(modifier = Modifier.height(Dimens.Margin.XSmall))
                    Text(
                        text = video.title,
                        maxLines = 2,
                        style = MaterialTheme.typography.labelMedium,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
