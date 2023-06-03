package cinescout.design.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import cinescout.design.AdaptivePreviews
import cinescout.design.theme.Dimens
import cinescout.resources.R.string
import cinescout.utils.compose.Adaptive
import cinescout.utils.compose.WindowWidthSizeClass
import cinescout.utils.kotlin.noop

@Composable
fun Modal(onDismiss: () -> Unit, content: @Composable BoxScope.() -> Unit) {
    Adaptive { windowSizeClass ->
        when (windowSizeClass.width) {
            WindowWidthSizeClass.Compact,
            WindowWidthSizeClass.Medium -> CsModalBottomSheet(
                onDismiss = onDismiss,
                content = content
            )
            WindowWidthSizeClass.Expanded -> CsDialog(
                onDismiss = onDismiss,
                content = content
            )
        }
    }
}

@Composable
private fun CsDialog(onDismiss: () -> Unit, content: @Composable BoxScope.() -> Unit) {
    val dialogContent = @Composable {
        Card {
            Box {
                IconButton(modifier = Modifier.align(Alignment.TopEnd), onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = string.close_button_description)
                    )
                }
                Box(modifier = Modifier.padding(top = Dimens.Margin.Medium, bottom = Dimens.Margin.Small)) {
                    content()
                }
            }
        }
    }
    Dialog(
        onDismissRequest = onDismiss,
        content = dialogContent
    )
}

@Composable
private fun CsModalBottomSheet(onDismiss: () -> Unit, content: @Composable BoxScope.() -> Unit) {
    val bottomSheetContent = @Composable { Box { content() } }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        content = { bottomSheetContent() }
    )
}

@Composable
@AdaptivePreviews.WithSystemUi
fun ModalPreview() {
    Modal(onDismiss = noop) {
        Text(text = "Content")
    }
}
