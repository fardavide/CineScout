package cinescout.android.setup

import cinescout.android.CineScoutApplication
import coil.Coil
import coil.ImageLoader
import coil.util.DebugLogger
import studio.forface.cinescout.BuildConfig

context(CineScoutApplication)
internal fun setupCoil() {
    Coil.setImageLoader {
        ImageLoader.Builder(this@CineScoutApplication)
            .crossfade(true)
            .logger(Logger())
            .build()
    }
}

private fun Logger() =
    if (BuildConfig.DEBUG) DebugLogger() else null
