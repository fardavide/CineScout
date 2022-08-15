package cinescout.android

import android.content.Context
import android.content.pm.PackageManager
import androidx.work.WorkManager
import cinescout.di.android.CineScoutAndroidModule
import cinescout.di.kotlin.AppVersionQualifier
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.dsl.module
import studio.forface.cinescout.BuildConfig

val AppModule = module {

    includes(CineScoutAndroidModule)

    single(AppVersionQualifier) { BuildConfig.VERSION_CODE }
    single { CineScoutLogger() }
    single { CoroutineScope(Job() + Dispatchers.Default) }
    factory { FirebaseAnalytics.getInstance(get()) }
    factory<PackageManager> { get<Context>().packageManager }
    factory { get<Context>().resources }
    factory { WorkManager.getInstance(get()) }
}
