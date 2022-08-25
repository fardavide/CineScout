package cinescout.lists.presentation.viewmodel

import cinescout.lists.presentation.model.WatchlistAction
import cinescout.lists.presentation.model.WatchlistState
import cinescout.utils.android.CineScoutViewModel

internal class WatchlistViewModel : CineScoutViewModel<WatchlistAction, WatchlistState>(WatchlistState.Loading) {

    init {
        updateState { WatchlistState.Data.Empty }
    }

    override fun submit(action: WatchlistAction) {
        // No actions
    }
}
