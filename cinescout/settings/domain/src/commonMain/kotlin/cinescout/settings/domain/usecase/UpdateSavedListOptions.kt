package cinescout.settings.domain.usecase

import cinescout.settings.domain.model.SavedListOptions

interface UpdateSavedListOptions {

    suspend operator fun invoke(savedListOptions: SavedListOptions)
}

class FakeUpdateSavedListOptions : UpdateSavedListOptions {

    var lastSavedListOptions: SavedListOptions? = null

    override suspend fun invoke(savedListOptions: SavedListOptions) {
        lastSavedListOptions = savedListOptions
    }
}
