package cinescout.design.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.SelectableChipElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.theme.CineScoutTheme

private val NoChipElevation: SelectableChipElevation? = null

@Composable
fun CsFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    @SuppressLint("ComposableLambdaParameterNaming") label: @Composable () -> Unit
) {
    ElevatedFilterChip(selected = selected, onClick = onClick, label = label, elevation = NoChipElevation)
}

@Preview
@Composable
private fun CsFilterChipPreview() {
    CineScoutTheme {
        Row {
            CsFilterChip(selected = false, onClick = {}, label = { Text(text = "Not selected") })
            CsFilterChip(selected = true, onClick = {}, label = { Text(text = "Selected") })
        }
    }
}
