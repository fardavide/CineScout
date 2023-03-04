package cinescout.details.presentation.sample

import cinescout.details.presentation.model.ScreenPlayRatingsUiModel
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.sample.MovieWithExtrasSample
import cinescout.tvshows.domain.sample.TvShowSample
import cinescout.tvshows.domain.sample.TvShowWithExtrasSample

internal object ScreenPlayRatingsUiModelSample {

    val BreakingBad = ScreenPlayRatingsUiModel(
        publicAverage = TvShowSample.BreakingBad.rating.average.value.toString(),
        publicCount = TvShowSample.BreakingBad.rating.voteCount.toString(),
        personal = TvShowWithExtrasSample.BreakingBad.personalRating.fold(
            ifEmpty = { ScreenPlayRatingsUiModel.Personal.NotRated },
            ifSome = { rating ->
                ScreenPlayRatingsUiModel.Personal.Rated(
                    rating = rating,
                    stringValue = rating.value.toInt().toString()
                )
            }
        )
    )

    val Grimm = ScreenPlayRatingsUiModel(
        publicAverage = TvShowSample.Grimm.rating.average.value.toString(),
        publicCount = TvShowSample.Grimm.rating.voteCount.toString(),
        personal = TvShowWithExtrasSample.Grimm.personalRating.fold(
            ifEmpty = { ScreenPlayRatingsUiModel.Personal.NotRated },
            ifSome = { rating ->
                ScreenPlayRatingsUiModel.Personal.Rated(
                    rating = rating,
                    stringValue = rating.value.toInt().toString()
                )
            }
        )
    )

    val Inception = ScreenPlayRatingsUiModel(
        publicAverage = MovieSample.Inception.rating.average.value.toString(),
        publicCount = MovieSample.Inception.rating.voteCount.toString(),
        personal = MovieWithExtrasSample.Inception.personalRating.fold(
            ifEmpty = { ScreenPlayRatingsUiModel.Personal.NotRated },
            ifSome = { rating ->
                ScreenPlayRatingsUiModel.Personal.Rated(
                    rating = rating,
                    stringValue = rating.value.toInt().toString()
                )
            }
        )
    )

    val TheWolfOfWallStreet = ScreenPlayRatingsUiModel(
        publicAverage = MovieSample.TheWolfOfWallStreet.rating.average.value.toString(),
        publicCount = MovieSample.TheWolfOfWallStreet.rating.voteCount.toString(),
        personal = MovieWithExtrasSample.TheWolfOfWallStreet.personalRating.fold(
            ifEmpty = { ScreenPlayRatingsUiModel.Personal.NotRated },
            ifSome = { rating ->
                ScreenPlayRatingsUiModel.Personal.Rated(
                    rating = rating,
                    stringValue = rating.value.toInt().toString()
                )
            }
        )
    )
}
