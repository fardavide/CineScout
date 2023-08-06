package cinescout.profile.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.util.NoContentDescription
import cinescout.resources.ImageRes
import cinescout.resources.R.drawable
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.resources.image
import cinescout.resources.string

@Composable
internal fun ProfileScreenItem(onClick: () -> Unit, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onClick)
            .padding(horizontal = Dimens.Margin.Medium, vertical = Dimens.Margin.Small)
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Composable
internal fun StaticProfileScreenItem(
    title: TextRes,
    icon: ImageRes,
    onClick: () -> Unit
) {
    ProfileScreenItem(onClick = onClick) {
        Image(
            modifier = Modifier
                .size(Dimens.Image.XSmall)
                .clip(CircleShape),
            painter = image(imageRes = icon),
            contentDescription = NoContentDescription
        )
        Spacer(modifier = Modifier.width(Dimens.Margin.Medium))
        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly) {
            Text(
                text = string(textRes = title),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview
@Composable
private fun ProfileScreenItemPreview() {
    CineScoutTheme {
        ProfileScreenItem(onClick = {}) {
            Text(text = "Content")
        }
    }
}

@Preview
@Composable
private fun StaticProfileScreenItemPreview() {
    CineScoutTheme {
        StaticProfileScreenItem(
            title = TextRes(string.settings),
            icon = ImageRes(drawable.ic_setting_color),
            onClick = {}
        )
    }
}
