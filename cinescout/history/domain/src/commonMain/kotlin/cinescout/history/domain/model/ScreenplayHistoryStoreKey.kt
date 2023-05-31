package cinescout.history.domain.model

import cinescout.screenplay.domain.model.ids.ScreenplayIds

sealed interface ScreenplayHistoryStoreKey {

    data class Read(val screenplayIds: ScreenplayIds) : ScreenplayHistoryStoreKey

    sealed interface Write : ScreenplayHistoryStoreKey {

        data class Add(val screenplayIds: ScreenplayIds) : Write

        data class Delete(val screenplayId: ScreenplayIds) : Write
    }
}
