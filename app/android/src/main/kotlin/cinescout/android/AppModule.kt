package cinescout.android

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.work.WorkManager
import cinescout.di.android.CineScoutAndroidModule
import cinescout.di.kotlin.AppVersionQualifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import studio.forface.cinescout.BuildConfig

@ComponentScan
@Module(includes = [CineScoutAndroidModule::class])
class AppModule {

    @Single
    @AppVersionQualifier
    fun appVersion(): Int = BuildConfig.VERSION_CODE

    @Single
    fun coroutineScope() = CoroutineScope(context = Job() + Dispatchers.Default)

    @Factory
    fun connectivityManager(context: Context) = context.getSystemService<ConnectivityManager>()!!

    @Factory
    fun notificationManagerCompat(context: Context) = NotificationManagerCompat.from(context)

    @Factory
    fun packageManager(context: Context) = context.packageManager

    @Factory
    fun resources(context: Context) = context.resources

    @Factory
    fun workManager(context: Context) = WorkManager.getInstance(context)
}
