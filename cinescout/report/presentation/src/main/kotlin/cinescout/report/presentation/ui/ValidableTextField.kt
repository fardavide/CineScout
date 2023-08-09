package cinescout.report.presentation.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import arrow.core.getOrElse
import cinescout.report.presentation.model.TextFieldState
import cinescout.resources.TextRes
import cinescout.resources.string

@Composable
@Suppress("UseComposableActions")
internal fun ValidableTextField(
    state: TextFieldState,
    onStateChange: (TextFieldState) -> Unit,
    label: TextRes,
    placeholder: TextRes,
    onFocused: () -> Unit,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    validate: () -> Unit
) {
    val singleLine = minLines == 1

    val text = remember(state.text) { state.text }
    var value by remember(text) { mutableStateOf(TextFieldValue(text = text, selection = TextRange(text.length))) }
    var hadFocus by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    hadFocus = true
                    onFocused()
                } else {
                    if (hadFocus) validate()
                }
            },
        value = value,
        onValueChange = {
            value = it
            onStateChange(state.copy(text = it.text))
        },
        minLines = minLines,
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = if (singleLine) ImeAction.Next else ImeAction.Default
        ),
        isError = state.error.isSome(),
        label = {
            val textRes = state.error.getOrElse { label }
            Text(text = string(textRes))
        },
        placeholder = { Text(text = string(placeholder)) }
    )
}
