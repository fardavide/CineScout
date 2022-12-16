package cinescout.design.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import studio.forface.cinescout.design.R

object PreviewUtils {

    val CineScoutIconDrawable
        @Composable get() =
            LocalContext.current.getDrawable(R.drawable.img_launcher_foreground)!!

    object Dimens {

        object Medium {

            const val Width = 540
            const val Height = 900
        }
    }
}

abstract class PreviewData<T : Any>(vararg data: T) : PreviewParameterProvider<T> {

    override val values = data.asSequence()
}
