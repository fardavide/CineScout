package cinescout.android.startup

import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.StartUpdateSuggestions
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object SyncStartup : Startup, KoinComponent {

    private val startUpdateSuggestions: StartUpdateSuggestions by inject()

    override fun init() {
        startUpdateSuggestions(SuggestionsMode.Deep)
    }
}
