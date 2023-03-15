package cinescout.rating.domain.model

import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId

sealed interface ScreenplayPersonalRatingsStoreKey {

    data class Read(val type: ScreenplayType) : ScreenplayPersonalRatingsStoreKey

    sealed interface Write : ScreenplayPersonalRatingsStoreKey {

        data class Add(val screenplayId: TmdbScreenplayId, val rating: Rating) : Write

        data class Remove(val screenplayId: TmdbScreenplayId) : Write
    }
}
