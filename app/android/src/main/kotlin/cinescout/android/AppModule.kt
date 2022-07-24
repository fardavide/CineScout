package cinescout.android

import android.content.Context
import android.content.pm.PackageManager
import androidx.work.WorkManager
import cinescout.di.CineScoutModule
import co.touchlab.kermit.Logger
import co.touchlab.kermit.StaticConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.dsl.module

val AppModule = module {

    includes(CineScoutModule)

    single { CoroutineScope(Job() + Dispatchers.Default) }
    single { Logger(StaticConfig()) }
    factory<PackageManager> { get<Context>().packageManager }
    factory { get<Context>().resources }
    factory { WorkManager.getInstance(get()) }
}
