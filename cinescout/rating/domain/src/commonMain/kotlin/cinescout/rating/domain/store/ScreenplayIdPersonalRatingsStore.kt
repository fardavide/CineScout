package cinescout.rating.domain.store

import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.rating.domain.model.ScreenplayPersonalRatingsStoreKey
import cinescout.store5.MutableStore5

interface ScreenplayIdPersonalRatingsStore :
    MutableStore5<ScreenplayPersonalRatingsStoreKey, List<ScreenplayIdWithPersonalRating>, Unit>
