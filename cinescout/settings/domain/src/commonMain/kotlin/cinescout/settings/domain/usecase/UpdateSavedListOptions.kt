package cinescout.settings.domain.usecase

import cinescout.settings.domain.model.SavedListOptions
import cinescout.settings.domain.repository.SettingsRepository
import org.koin.core.annotation.Factory

interface UpdateSavedListOptions {

    suspend operator fun invoke(savedListOptions: SavedListOptions)
}

@Factory
internal class RealUpdateSavedListOptions(
    private val settingsRepository: SettingsRepository
) : UpdateSavedListOptions {

    override suspend operator fun invoke(savedListOptions: SavedListOptions) {
        settingsRepository.updateSavedListOptions(savedListOptions)
    }
}

class FakeUpdateSavedListOptions : UpdateSavedListOptions {

    var lastSavedListOptions: SavedListOptions? = null

    override suspend fun invoke(savedListOptions: SavedListOptions) {
        lastSavedListOptions = savedListOptions
    }
}
