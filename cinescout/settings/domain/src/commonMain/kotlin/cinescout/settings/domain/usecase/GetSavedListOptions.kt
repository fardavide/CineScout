package cinescout.settings.domain.usecase

import arrow.core.Option
import cinescout.settings.domain.model.SavedListOptions
import cinescout.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.Factory

interface GetSavedListOptions {

    operator fun invoke(): StateFlow<Option<SavedListOptions>>
}

@Factory
internal class RealGetSavedListOptions(
    private val settingsRepository: SettingsRepository
) : GetSavedListOptions {

    override operator fun invoke(): StateFlow<Option<SavedListOptions>> =
        settingsRepository.getSavedListOptions()
}

class FakeGetSavedListOptions(
    savedListOptions: SavedListOptions? = null
) : GetSavedListOptions {

    private val stateFlow = MutableStateFlow(Option.fromNullable(savedListOptions))

    override fun invoke() = stateFlow
}
