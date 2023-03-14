package cinescout.details.presentation.sample

import cinescout.details.presentation.model.ScreenplayRatingsUiModel
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.sample.ScreenpalyWithExtrasSample
import cinescout.tvshows.domain.sample.TvShowSample

internal object ScreenPlayRatingsUiModelSample {

    val BreakingBad = ScreenplayRatingsUiModel(
        publicAverage = TvShowSample.BreakingBad.rating.average.value.toString(),
        publicCount = TvShowSample.BreakingBad.rating.voteCount.toString(),
        personal = TvShowWithExtrasSample.BreakingBad.personalRating.fold(
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
        publicAverage = TvShowSample.Grimm.rating.average.value.toString(),
        publicCount = TvShowSample.Grimm.rating.voteCount.toString(),
        personal = TvShowWithExtrasSample.Grimm.personalRating.fold(
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
        publicAverage = MovieSample.Inception.rating.average.value.toString(),
        publicCount = MovieSample.Inception.rating.voteCount.toString(),
        personal = ScreenpalyWithExtrasSample.Inception.personalRating.fold(
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
        publicAverage = MovieSample.TheWolfOfWallStreet.rating.average.value.toString(),
        publicCount = MovieSample.TheWolfOfWallStreet.rating.voteCount.toString(),
        personal = ScreenpalyWithExtrasSample.TheWolfOfWallStreet.personalRating.fold(
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
