package cinescout.report.presentation.model

import arrow.core.Option
import arrow.core.none
import arrow.optics.optics
import cinescout.resources.TextRes

@optics data class TextFieldState(
    val text: String,
    val error: Option<TextRes>
) {

    companion object {

        val Empty = TextFieldState(
            text = "",
            error = none()
        )
    }
}
