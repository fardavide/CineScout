package cinescout.android

import android.app.Application
import cinescout.android.setup.setupCoil
import cinescout.android.setup.setupKoin
import cinescout.android.setup.setupLogger
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.StartUpdateSuggestions
import org.koin.android.ext.android.inject

class CineScoutApplication : Application() {

    private val startUpdateSuggestions: StartUpdateSuggestions by inject()

    override fun onCreate() {
        super.onCreate()

        setupKoin()
        setupLogger()
        setupCoil()

        // Update suggestions 
        startUpdateSuggestions(SuggestionsMode.Deep)
    }
}
