package cinescout.design.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import studio.forface.cinescout.design.R

object PreviewUtils {

    val CineScoutIconDrawable @Composable get() =
        LocalContext.current.getDrawable(R.drawable.img_launcher_foreground)!!

    object Dimens {

        object Medium {

            const val Width = 540
            const val Height = 900
        }
    }
}
