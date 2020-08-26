package client.android.util

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import client.android.theme.CineScoutTheme

@Composable
internal fun ThemedPreview(
    darkTheme: Boolean = false,
    children: @Composable () -> Unit
) {
    CineScoutTheme(darkTheme = darkTheme) {
        Surface {
            children()
        }
    }
}
