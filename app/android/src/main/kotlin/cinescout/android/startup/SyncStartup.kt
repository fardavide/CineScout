package cinescout.android.startup

import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.ScheduleUpdateSuggestions
import cinescout.sync.domain.usecase.ScheduleAutomatedSync
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object SyncStartup : Startup, KoinComponent {

    private val scheduleAutomatedSync: ScheduleAutomatedSync by inject()
    private val scheduleUpdateSuggestions: ScheduleUpdateSuggestions by inject()

    override fun init() {
        scheduleAutomatedSync()
        scheduleUpdateSuggestions(SuggestionsMode.Deep)
    }
}
