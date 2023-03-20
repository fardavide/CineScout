package cinescout.resources

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.res.stringResource

@Immutable
sealed interface TextRes {

    data class Plain(val value: String) : TextRes

    @JvmInline
    value class Resource(@StringRes val resId: Int) : TextRes

    data class ResourceWithArgs(
        @StringRes val resId: Int,
        val args: List<Any>
    ) : TextRes

    companion object {

        operator fun invoke(string: String): TextRes = Plain(string)

        operator fun invoke(@StringRes resId: Int): TextRes = Resource(resId)

        operator fun invoke(@StringRes resId: Int, vararg args: Any): TextRes =
            ResourceWithArgs(resId, args.toList())
    }
}

@Composable
fun string(textRes: TextRes): String = when (textRes) {
    is TextRes.Plain -> textRes.value
    is TextRes.Resource -> stringResource(id = textRes.resId)
    is TextRes.ResourceWithArgs -> stringResource(id = textRes.resId, *textRes.args.toTypedArray())
}


