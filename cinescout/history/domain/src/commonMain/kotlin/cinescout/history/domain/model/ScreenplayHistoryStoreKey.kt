package cinescout.history.domain.model

import cinescout.screenplay.domain.model.ScreenplayIds

sealed interface ScreenplayHistoryStoreKey {

    data class Read(val screenplayIds: ScreenplayIds) : ScreenplayHistoryStoreKey

    sealed interface Write : ScreenplayHistoryStoreKey {

        data class Add(val screenplayIds: ScreenplayIds) : Write

        data class Remove(val screenplayId: ScreenplayIds) : Write
    }
}
