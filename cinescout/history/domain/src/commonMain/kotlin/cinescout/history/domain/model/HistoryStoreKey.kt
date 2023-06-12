package cinescout.history.domain.model

import cinescout.screenplay.domain.model.ids.ScreenplayIds

sealed interface HistoryStoreKey {

    data class Read(val screenplayIds: ScreenplayIds) : HistoryStoreKey

    sealed interface Write : HistoryStoreKey {

        data class Add(val screenplayIds: ScreenplayIds) : Write

        data class Delete(val screenplayId: ScreenplayIds) : Write
    }
}
