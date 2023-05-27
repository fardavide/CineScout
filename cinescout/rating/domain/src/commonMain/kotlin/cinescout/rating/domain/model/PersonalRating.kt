package cinescout.rating.domain.model

import arrow.core.Option
import cinescout.screenplay.domain.model.Rating

data class PersonalRating(val value: Option<Rating>)
