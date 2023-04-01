package cinescout.android.startup

import cinescout.android.AppModule
import cinescout.android.CineScoutApplicationContext
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

object KoinStartup : Startup {

    context(CineScoutApplicationContext)
    override fun init() {
        startKoin {
            androidLogger()
            androidContext(app)
            workManagerFactory()
            modules(AppModule().module)
        }
    }
}
