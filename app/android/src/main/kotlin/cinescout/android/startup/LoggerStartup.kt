package cinescout.android.startup

import cinescout.android.CineScoutApplicationContext
import cinescout.android.CineScoutLogger

object LoggerStartup : Startup {

    context(CineScoutApplicationContext)
    override fun init() {
        CineScoutLogger()
    }
}
