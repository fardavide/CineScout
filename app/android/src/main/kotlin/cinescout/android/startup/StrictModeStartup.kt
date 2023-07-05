package cinescout.android.startup

import android.os.Build
import android.os.StrictMode
import android.os.strictmode.Violation
import androidx.annotation.RequiresApi
import cinescout.android.CineScoutApplicationContext
import cinescout.android.MainActivity
import studio.forface.cinescout.BuildConfig

object StrictModeStartup : Startup {

    context(CineScoutApplicationContext)
    override fun init() {
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            enableStrictMode()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyListener(executor(), ::throwIfNotWhitelisted)
                .build()
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectAll()
                .setClassInstanceLimit(MainActivity::class.java, 3)
                .penaltyListener(executor(), ::throwIfNotWhitelisted)
                .build()
        )
    }

    private fun throwIfNotWhitelisted(violation: Violation) {
        if (isNotWhitelisted(violation)) {
            throw violation
        }
    }

    private fun isNotWhitelisted(violation: Violation) = violation.stackTrace.none { stackTraceElement ->
        Whitelist.any { whitelistedMethod -> whitelistedMethod in stackTraceElement.toString() }
    }

    private fun executor() = { runnable: Runnable -> runnable.run() }
}

private val Whitelist = listOf(
    "com.google.firebase.perf.FirebasePerformance.<init>",
    "com.google.firebase.perf.metrics.AppStartTrace.onActivityResumed",
    "com.skydoves.landscapist.coil.CoilImage__CoilImageKt\$CoilImage\$8.invoke",
    "dalvik.system.BaseDexClassLoader.findResources",
    "okhttp3.internal.connection.RealConnection.connectSocket",
    "sun.nio.fs.UnixDirectoryStream.finalize"
)
