package cinescout.design.theme

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Modifier.imageBackground() =
    background(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))
