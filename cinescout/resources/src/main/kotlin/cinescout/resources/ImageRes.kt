package cinescout.resources

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource

@Immutable
sealed interface ImageRes : Resource {

    @JvmInline
    value class Vector(val value: ImageVector) : ImageRes

    @JvmInline
    value class Resource(@DrawableRes val resId: Int) : ImageRes

    companion object {

        operator fun invoke(vector: ImageVector): ImageRes = Vector(vector)

        operator fun invoke(@DrawableRes resId: Int): ImageRes = Resource(resId)
    }
}

@Composable
fun image(imageRes: ImageRes): Painter = when (imageRes) {
    is ImageRes.Vector -> rememberVectorPainter(image = imageRes.value)
    is ImageRes.Resource -> painterResource(id = imageRes.resId)
}
