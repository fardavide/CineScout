package cinescout.design.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.TestTag
import cinescout.design.theme.Dimens
import cinescout.design.util.blend
import cinescout.resources.TextRes
import cinescout.resources.string
import cinescout.utils.compose.Adaptive
import cinescout.utils.compose.WindowWidthSizeClass

@Composable
fun Banner(
    type: Banner.Type,
    message: TextRes,
    modifier: Modifier = Modifier
) {
    Adaptive { windowSizeClass ->
        Box(
            modifier = modifier
                .testTag(TestTag.Banner)
                .fillMaxWidth()
                .background(color = type.containerColor())
                .let { if (windowSizeClass.width == WindowWidthSizeClass.Compact) it.statusBarsPadding() else it }
                .padding(Dimens.Margin.Small),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = string(textRes = message),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

object Banner {

    enum class Type(val containerColor: @Composable () -> Color) {

        Error({ MaterialTheme.colorScheme.errorContainer }),
        Info({ MaterialTheme.colorScheme.tertiaryContainer }),
        Success({ MaterialTheme.colorScheme.secondaryContainer }),
        Warning({ Color.blend(MaterialTheme.colorScheme.errorContainer, Color.Yellow, ratio = 0.75f) })
    }
}

@Preview
@Composable
private fun ErrorBannerPreview() {
    Banner(type = Banner.Type.Error, message = TextRes("Error"))
}

@Preview
@Composable
private fun InfoBannerPreview() {
    Banner(type = Banner.Type.Info, message = TextRes("Info"))
}

@Preview
@Composable
private fun SuccessBannerPreview() {
    Banner(type = Banner.Type.Success, message = TextRes("Success"))
}

@Preview
@Composable
private fun WarningBannerPreview() {
    Banner(type = Banner.Type.Warning, message = TextRes("Warning"))
}
