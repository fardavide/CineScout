package cinescout.rating.domain.store

import cinescout.rating.domain.model.PersonalRatingsStoreKey
import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.store5.MutableStore5

interface PersonalRatingIdsStore :
    MutableStore5<PersonalRatingsStoreKey, List<ScreenplayIdWithPersonalRating>, Unit>
