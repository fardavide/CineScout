package cinescout.settings.domain.model

import arrow.core.Option

data class AppSettings(
    val savedListOptions: Option<SavedListOptions>,
    val suggestionSettings: SuggestionSettings
)
