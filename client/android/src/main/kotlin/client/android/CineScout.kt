@file:Suppress("PackageDirectoryMismatch")
package studio.forface.cinescout

import android.app.Application
import client.android.androidClientModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CineScout : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CineScout)
            modules(androidClientModule)
        }
    }
}
