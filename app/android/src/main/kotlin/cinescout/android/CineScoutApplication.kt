package cinescout.android

import android.app.Application
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.StartUpdateSuggestedMovies
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.GlobalContext.startKoin

class CineScoutApplication : Application() {

    private val startUpdateSuggestedMovies: StartUpdateSuggestedMovies by inject()

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidLogger()
            androidContext(this@CineScoutApplication)
            workManagerFactory()
            modules(AppModule)
        }.koin

        // Init logger
        CineScoutLogger()

        // Update suggestions 
        startUpdateSuggestedMovies(SuggestionsMode.Deep)
    }
}
