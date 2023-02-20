package cinescout.design.util

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import cinescout.design.R.drawable

object PreviewUtils {

    val CineScoutIconDrawable
        @Composable get() =
            AppCompatResources.getDrawable(LocalContext.current, drawable.ic_movie_camera)!!

    object Dimens {

        object Medium {

            const val Width = 540
            const val Height = 900
        }
    }
}

abstract class PreviewDataProvider<T : Any>(first: T, vararg others: T) : PreviewParameterProvider<T> {

    override val values = (listOf(first) + others).asSequence()
}
