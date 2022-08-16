package cinescout.android

import android.content.Context
import android.content.pm.PackageManager
import androidx.work.WorkManager
import cinescout.di.android.CineScoutAndroidModule
import cinescout.di.kotlin.AppVersionQualifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.dsl.module
import studio.forface.cinescout.BuildConfig

val AppModule = module {

    includes(CineScoutAndroidModule)

    single(AppVersionQualifier) { BuildConfig.VERSION_CODE }
    single { CineScoutLogger() }
    single { CoroutineScope(context = Job() + Dispatchers.Default) }
    single { CineScoutAnalytics(context = get()) }
    factory<PackageManager> { get<Context>().packageManager }
    factory { get<Context>().resources }
    factory { WorkManager.getInstance(get()) }
}
