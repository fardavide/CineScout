package cinescout.details.presentation.model

import arrow.core.Option
import arrow.core.none
import arrow.core.some
import cinescout.common.model.Rating

data class ScreenPlayRatingsUiModel(
    val publicAverage: String,
    val publicCount: String,
    val personal: Personal
) {

    sealed class Personal(open val rating: Option<Rating>) {

        data class Rated(override val rating: Option<Rating>, val stringValue: String) : Personal(rating) {
            constructor(rating: Rating, stringValue: String) : this(rating.some(), stringValue)
        }

        object NotRated : Personal(none())
    }
}
