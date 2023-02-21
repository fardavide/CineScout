package cinescout.android

import android.app.Application
import cinescout.android.startup.CoilStartup
import cinescout.android.startup.KoinStartup
import cinescout.android.startup.LoggerStartup
import cinescout.android.startup.SyncStartup
import cinescout.android.startup.init

class CineScoutApplication : Application(), CineScoutApplicationContext {

    override val app = this

    override fun onCreate() {
        super.onCreate()

        init(CoilStartup, KoinStartup, LoggerStartup, SyncStartup)
    }
}

interface CineScoutApplicationContext {
    val app: CineScoutApplication
}
