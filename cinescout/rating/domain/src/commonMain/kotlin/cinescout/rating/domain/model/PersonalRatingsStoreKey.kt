package cinescout.rating.domain.model

import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId

sealed interface PersonalRatingsStoreKey {

    data class Read(val type: ScreenplayTypeFilter) : PersonalRatingsStoreKey

    sealed interface Write : PersonalRatingsStoreKey {

        data class Add(val screenplayIds: ScreenplayIds, val rating: Rating) : Write

        data class Remove(val screenplayId: TmdbScreenplayId) : Write
    }
}
