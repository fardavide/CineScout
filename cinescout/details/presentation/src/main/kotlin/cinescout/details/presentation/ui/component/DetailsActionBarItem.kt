package cinescout.details.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.util.NoContentDescription
import cinescout.details.presentation.model.DetailsActionItemUiModel
import cinescout.details.presentation.previewdata.DetailsActionItemUiModelPreviewProvider
import cinescout.resources.ImageRes
import cinescout.resources.TextRes
import cinescout.resources.image
import cinescout.resources.string

@Composable
internal fun DetailsActionBarItem(uiModel: DetailsActionItemUiModel, onClick: () -> Unit) {
    Box(contentAlignment = Alignment.TopEnd) {
        IconButton(onClick = onClick) {
            Icon(
                painter = image(imageRes = uiModel.imageRes),
                contentDescription = string(textRes = uiModel.contentDescription)
            )
        }
        Box(modifier = Modifier.padding(Dimens.Margin.xSmall)) {
            uiModel.badgeResource.onSome { badgeResource ->
                when (badgeResource) {
                    is ImageRes -> Image(
                        modifier = Modifier.size(Dimens.Icon.xSmall),
                        painter = image(badgeResource),
                        contentDescription = NoContentDescription
                    )

                    is TextRes -> Text(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                            .padding(horizontal = Dimens.Margin.xSmall, vertical = Dimens.Margin.xxxSmall),
                        text = string(badgeResource),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DetailsActionBarItemPreview(
    @PreviewParameter(DetailsActionItemUiModelPreviewProvider::class) uiModel: DetailsActionItemUiModel
) {
    CineScoutTheme {
        DetailsActionBarItem(uiModel, onClick = {})
    }
}
