package cinescout.settings.domain.usecase

import arrow.core.Option
import cinescout.settings.domain.model.SavedListOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface GetSavedListOptions {

    operator fun invoke(): StateFlow<Option<SavedListOptions>>
}

class FakeGetSavedListOptions(
    savedListOptions: SavedListOptions? = null
) : GetSavedListOptions {

    private val stateFlow = MutableStateFlow(Option.fromNullable(savedListOptions))

    override fun invoke() = stateFlow
}
