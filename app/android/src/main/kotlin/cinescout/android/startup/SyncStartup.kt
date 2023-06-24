package cinescout.android.startup

import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.StartUpdateSuggestions
import cinescout.sync.automated.usecase.StartAutomatedSync
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object SyncStartup : Startup, KoinComponent {

    private val startAutomatedSync: StartAutomatedSync by inject()
    private val startUpdateSuggestions: StartUpdateSuggestions by inject()

    override fun init() {
        startAutomatedSync()
        startUpdateSuggestions(SuggestionsMode.Deep)
    }
}
