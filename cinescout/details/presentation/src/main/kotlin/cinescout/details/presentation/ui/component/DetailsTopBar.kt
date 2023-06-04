package cinescout.details.presentation.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.theme.CineScoutTheme
import cinescout.resources.R.string

@Composable
internal fun DetailsTopBar(back: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier.statusBarsPadding()) {
        FilledTonalIconButton(onClick = back) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = string.back_button_description)
            )
        }
    }
}

@Preview
@Composable
private fun DetailsTopBarPreview() {
    CineScoutTheme {
        DetailsTopBar(back = {})
    }
}
