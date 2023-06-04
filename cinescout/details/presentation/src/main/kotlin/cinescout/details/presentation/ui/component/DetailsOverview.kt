package cinescout.details.presentation.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun DetailsOverview(overview: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = overview,
        style = MaterialTheme.typography.bodyMedium
    )
}
