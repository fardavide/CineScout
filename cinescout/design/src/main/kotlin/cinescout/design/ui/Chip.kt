package cinescout.design.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ChipElevation
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.theme.surfaceThin
import cinescout.design.util.NoContentDescription

private val NoChipElevation: ChipElevation? = null
private val NoSelectableChipElevation: SelectableChipElevation? = null

private val containerColor @Composable get() = MaterialTheme.colorScheme.surfaceThin

@Composable
fun CsAssistChip(
    onClick: () -> Unit = {},
    trailingIcon: @Composable (() -> Unit)? = null,
    @SuppressLint("ComposableLambdaParameterNaming") label: @Composable () -> Unit
) {
    ElevatedAssistChip(
        onClick = onClick,
        label = label,
        trailingIcon = trailingIcon,
        colors = AssistChipDefaults.elevatedAssistChipColors(containerColor = containerColor),
        elevation = NoChipElevation
    )
}

@Composable
fun CsDropdownChip(
    onClick: () -> Unit = {},
    @SuppressLint("ComposableLambdaParameterNaming") label: @Composable () -> Unit
) {
    CsAssistChip(
        onClick = onClick,
        label = {
            label()
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = NoContentDescription
            )
        }
    )
}

@Composable
fun CsFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    @SuppressLint("ComposableLambdaParameterNaming") label: @Composable () -> Unit
) {
    ElevatedFilterChip(
        selected = selected,
        onClick = onClick,
        label = label,
        colors = FilterChipDefaults.filterChipColors(containerColor = containerColor),
        elevation = NoSelectableChipElevation
    )
}

@Composable
fun CsSuggestionChip(
    onClick: () -> Unit,
    @SuppressLint("ComposableLambdaParameterNaming") label: @Composable () -> Unit
) {
    ElevatedSuggestionChip(
        onClick = onClick,
        label = label,
        colors = AssistChipDefaults.elevatedAssistChipColors(containerColor = containerColor),
        elevation = NoChipElevation
    )
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
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.Margin.xxSmall)) {
            CsFilterChip(selected = false, onClick = {}, label = { Text(text = "Not selected Filter") })
            CsFilterChip(selected = true, onClick = {}, label = { Text(text = "Selected Filter") })
        }
    }
}

@Preview
@Composable
private fun CsSuggestionChipPreview() {
    CineScoutTheme {
        CsSuggestionChip(onClick = {}, label = { Text(text = "Suggestion") })
    }
}
