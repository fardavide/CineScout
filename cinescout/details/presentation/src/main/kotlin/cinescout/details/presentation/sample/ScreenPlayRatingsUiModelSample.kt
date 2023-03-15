package cinescout.details.presentation.sample

import cinescout.details.domain.sample.ScreenplayWithExtrasSample
import cinescout.details.presentation.model.ScreenplayRatingsUiModel
import cinescout.screenplay.domain.sample.ScreenplaySample

internal object ScreenPlayRatingsUiModelSample {

    val BreakingBad = ScreenplayRatingsUiModel(
        publicAverage = ScreenplaySample.BreakingBad.rating.average.value.toString(),
        publicCount = ScreenplaySample.BreakingBad.rating.voteCount.toString(),
        personal = ScreenplayWithExtrasSample.BreakingBad.personalRating.fold(
            ifEmpty = { ScreenplayRatingsUiModel.Personal.NotRated },
            ifSome = { rating ->
                ScreenplayRatingsUiModel.Personal.Rated(
                    rating = rating,
                    stringValue = rating.value.toInt().toString()
                )
            }
        )
    )

    val Grimm = ScreenplayRatingsUiModel(
        publicAverage = ScreenplaySample.Grimm.rating.average.value.toString(),
        publicCount = ScreenplaySample.Grimm.rating.voteCount.toString(),
        personal = ScreenplayWithExtrasSample.Grimm.personalRating.fold(
            ifEmpty = { ScreenplayRatingsUiModel.Personal.NotRated },
            ifSome = { rating ->
                ScreenplayRatingsUiModel.Personal.Rated(
                    rating = rating,
                    stringValue = rating.value.toInt().toString()
                )
            }
        )
    )

    val Inception = ScreenplayRatingsUiModel(
        publicAverage = ScreenplaySample.Inception.rating.average.value.toString(),
        publicCount = ScreenplaySample.Inception.rating.voteCount.toString(),
        personal = ScreenplayWithExtrasSample.Inception.personalRating.fold(
            ifEmpty = { ScreenplayRatingsUiModel.Personal.NotRated },
            ifSome = { rating ->
                ScreenplayRatingsUiModel.Personal.Rated(
                    rating = rating,
                    stringValue = rating.value.toInt().toString()
                )
            }
        )
    )

    val TheWolfOfWallStreet = ScreenplayRatingsUiModel(
        publicAverage = ScreenplaySample.TheWolfOfWallStreet.rating.average.value.toString(),
        publicCount = ScreenplaySample.TheWolfOfWallStreet.rating.voteCount.toString(),
        personal = ScreenplayWithExtrasSample.TheWolfOfWallStreet.personalRating.fold(
            ifEmpty = { ScreenplayRatingsUiModel.Personal.NotRated },
            ifSome = { rating ->
                ScreenplayRatingsUiModel.Personal.Rated(
                    rating = rating,
                    stringValue = rating.value.toInt().toString()
                )
            }
        )
    )
}
