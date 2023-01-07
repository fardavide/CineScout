package cinescout.android.setup

import cinescout.android.AppModule
import cinescout.android.CineScoutApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.GlobalContext

context(CineScoutApplication)
internal fun setupKoin() {
    GlobalContext.startKoin {
        androidLogger()
        androidContext(this@CineScoutApplication)
        workManagerFactory()
        modules(AppModule)
    }.koin
}
