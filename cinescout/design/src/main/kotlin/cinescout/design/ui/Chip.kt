package cinescout.design.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ChipElevation
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.SelectableChipElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.theme.CineScoutTheme

private val NoChipElevation: ChipElevation? = null
private val NoSelectableChipElevation: SelectableChipElevation? = null

@Composable
fun CsAssistChip(
    onClick: () -> Unit = {},
    @SuppressLint("ComposableLambdaParameterNaming") label: @Composable () -> Unit
) {
    ElevatedAssistChip(onClick = onClick, label = label, elevation = NoChipElevation)
}

@Composable
fun CsFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    @SuppressLint("ComposableLambdaParameterNaming") label: @Composable () -> Unit
) {
    ElevatedFilterChip(selected = selected, onClick = onClick, label = label, elevation = NoSelectableChipElevation)
}

@Preview
@Composable
private fun CsAssistChipPreview() {
    CineScoutTheme {
        CsAssistChip(label = { Text(text = "Assist") })
    }
}

@Preview
@Composable
private fun CsFilterChipPreview() {
    CineScoutTheme {
        Row {
            CsFilterChip(selected = false, onClick = {}, label = { Text(text = "Not selected Filter") })
            CsFilterChip(selected = true, onClick = {}, label = { Text(text = "Selected Filter") })
        }
    }
}
