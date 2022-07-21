package android

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.GlobalContext.startKoin

class CineScoutApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidLogger()
            androidContext(this@CineScoutApplication)
            workManagerFactory()
            modules(AppModule)
        }
    }
}
