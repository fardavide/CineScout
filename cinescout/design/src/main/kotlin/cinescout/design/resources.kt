package cinescout.design

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cinescout.error.NetworkError
import studio.forface.cinescout.design.R

sealed interface TextRes {

    @JvmInline
    value class Plain(val value: String) : TextRes

    @JvmInline
    value class Resource(@StringRes val resId: Int) : TextRes

    companion object {

        operator fun invoke(string: String): TextRes =
            Plain(string)

        operator fun invoke(@StringRes resId: Int): TextRes =
            Resource(resId)
    }
}

@Composable
fun stringResource(textRes: TextRes): String = when (textRes) {
    is TextRes.Plain -> textRes.value
    is TextRes.Resource -> stringResource(id = textRes.resId)
}

open class NetworkErrorToMessageMapper {

    open fun toMessage(networkError: NetworkError): TextRes {
        val resId = when (networkError) {
            NetworkError.Forbidden -> TODO()
            NetworkError.Internal -> TODO()
            NetworkError.NoNetwork -> R.string.error_no_network
            NetworkError.NotFound -> TODO()
            NetworkError.Unauthorized -> TODO()
            NetworkError.Unreachable -> TODO()
        }
        return TextRes(resId)
    }
}

