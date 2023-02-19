package cinescout.design

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cinescout.design.R.string
import cinescout.design.testdata.MessageSample
import cinescout.error.NetworkError
import org.koin.core.annotation.Factory

sealed interface ImageRes {

    @JvmInline
    value class Vector(val value: ImageVector) : ImageRes

    @JvmInline
    value class Resource(@DrawableRes val resId: Int) : ImageRes

    companion object {

        operator fun invoke(vector: ImageVector): ImageRes = Vector(vector)

        operator fun invoke(@DrawableRes resId: Int): ImageRes = Resource(resId)
    }
}

sealed interface TextRes {

    data class Plain(val value: String) : TextRes

    @JvmInline
    value class Resource(@StringRes val resId: Int) : TextRes

    companion object {

        operator fun invoke(string: String): TextRes = Plain(string)

        operator fun invoke(@StringRes resId: Int): TextRes = Resource(resId)
    }
}

@Composable
fun image(imageRes: ImageRes): Painter = when (imageRes) {
    is ImageRes.Vector -> rememberVectorPainter(image = imageRes.value)
    is ImageRes.Resource -> painterResource(id = imageRes.resId)
}

@Composable
fun string(textRes: TextRes): String = when (textRes) {
    is TextRes.Plain -> textRes.value
    is TextRes.Resource -> stringResource(id = textRes.resId)
}

@Factory
open class NetworkErrorToMessageMapper {

    open fun toMessage(networkError: NetworkError): TextRes {
        val resId = when (networkError) {
            NetworkError.Forbidden -> string.network_error_forbidden
            NetworkError.Internal -> string.network_error_internal
            NetworkError.NoNetwork -> string.network_error_no_network
            NetworkError.NotFound -> string.network_error_not_found
            NetworkError.Unauthorized -> string.network_error_unauthorized
            NetworkError.Unknown -> string.network_error_unknown
            NetworkError.Unreachable -> string.network_error_unreachable
        }
        return TextRes(resId)
    }
}

class FakeNetworkErrorToMessageMapper(
    private val message: TextRes = MessageSample.NoNetworkError
) : NetworkErrorToMessageMapper() {

    override fun toMessage(networkError: NetworkError): TextRes = message
}
