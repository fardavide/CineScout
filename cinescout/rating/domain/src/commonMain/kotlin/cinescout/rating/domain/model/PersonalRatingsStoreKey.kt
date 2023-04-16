package cinescout.rating.domain.model

import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId

sealed interface PersonalRatingsStoreKey {

    data class Read(val type: ScreenplayType) : PersonalRatingsStoreKey

    sealed interface Write : PersonalRatingsStoreKey {

        data class Add(val screenplayIds: ScreenplayIds, val rating: Rating) : Write

        data class Remove(val screenplayId: TmdbScreenplayId) : Write
    }
}
