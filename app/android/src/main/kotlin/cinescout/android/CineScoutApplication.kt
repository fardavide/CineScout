package cinescout.android

import android.app.Application
import android.os.StrictMode
import cinescout.design.ImageLoaderFactory
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.StartUpdateSuggestedMovies
import coil.Coil
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.GlobalContext.startKoin

class CineScoutApplication : Application() {

    private val startUpdateSuggestedMovies: StartUpdateSuggestedMovies by inject()

    override fun onCreate() {
        super.onCreate()

        // Strict mode
        val threadPolicyBuilder = StrictMode.ThreadPolicy.Builder()
            .detectAll()
            .penaltyFlashScreen()
            .penaltyLog()

        val vmPolicyBuilder = StrictMode.VmPolicy.Builder()
            .detectActivityLeaks()
            .detectCleartextNetwork()
            .detectFileUriExposure()
            .detectContentUriWithoutPermission()
            .detectCredentialProtectedWhileLocked()
            .detectImplicitDirectBoot()
            .detectLeakedRegistrationObjects()
            .detectLeakedSqlLiteObjects()
            .detectNonSdkApiUsage()
            .penaltyListener(
                { command -> command.run() },
                { violation ->
                    violation.printStackTrace()
                    throw violation
                }
            )

        StrictMode.setThreadPolicy(threadPolicyBuilder.build())
        StrictMode.setVmPolicy(vmPolicyBuilder.build())

        // Start Koin
        startKoin {
            androidLogger()
            androidContext(this@CineScoutApplication)
            workManagerFactory()
            modules(AppModule)
        }.koin

        // Init logger
        CineScoutLogger()

        // Init Coil
        Coil.setImageLoader(ImageLoaderFactory(this))

        // Update suggestions 
        startUpdateSuggestedMovies(SuggestionsMode.Deep)
    }
}
