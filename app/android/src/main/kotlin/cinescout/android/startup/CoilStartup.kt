package cinescout.android.startup

import cinescout.android.CineScoutApplicationContext
import coil.Coil
import coil.ImageLoader
import coil.util.DebugLogger
import studio.forface.cinescout.BuildConfig

object CoilStartup : Startup {

    context(CineScoutApplicationContext)
    override fun init() {
        Coil.setImageLoader {
            ImageLoader.Builder(app)
                .crossfade(true)
                .logger(Logger())
                .build()
        }
    }

    private fun Logger() = if (BuildConfig.DEBUG) DebugLogger() else null
}
