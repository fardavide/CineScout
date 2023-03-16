package cinescout.android.startup

import cinescout.android.CineScoutApplicationContext
import cinescout.media.presentation.CoilMediaRequestInterceptor
import coil.Coil
import coil.ImageLoader
import coil.util.DebugLogger
import org.koin.android.ext.android.get
import studio.forface.cinescout.BuildConfig

object CoilStartup : Startup {

    context(CineScoutApplicationContext)
    override fun init() {
        Coil.setImageLoader {
            ImageLoader.Builder(app)
                .components {
                    add(app.get<CoilMediaRequestInterceptor>())
                }
                .crossfade(true)
                .logger(Logger())
                .build()
        }
    }

    private fun Logger() = if (BuildConfig.DEBUG) DebugLogger() else null
}
