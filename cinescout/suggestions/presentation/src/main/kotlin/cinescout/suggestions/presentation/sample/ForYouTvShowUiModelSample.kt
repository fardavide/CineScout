package cinescout.suggestions.presentation.sample

import cinescout.common.model.TmdbBackdropImage
import cinescout.common.model.TmdbPosterImage
import cinescout.common.model.TmdbProfileImage
import cinescout.suggestions.presentation.model.ForYouTvShowUiModel
import cinescout.tvshows.domain.testdata.TvShowCreditsTestData
import cinescout.tvshows.domain.testdata.TvShowTestData
import cinescout.tvshows.domain.testdata.TvShowWithExtrasTestData

object ForYouTvShowUiModelSample {

    val BreakingBad = ForYouTvShowUiModel(
        tmdbTvShowId = TvShowTestData.BreakingBad.tmdbId,
        actors = TvShowCreditsTestData.BreakingBad.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouTvShowUiModel.Actor(imageUrl.orNull().orEmpty())
        },
        backdropUrl = TvShowTestData.BreakingBad.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        firstAirDate = TvShowTestData.BreakingBad.firstAirDate.year.toString(),
        genres = TvShowWithExtrasTestData.BreakingBad.tvShowWithDetails.genres.map { genre -> genre.name },
        posterUrl = TvShowTestData.BreakingBad.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = TvShowTestData.BreakingBad.rating.average.value.toString(),
        title = TvShowTestData.BreakingBad.title
    )

    val Dexter = ForYouTvShowUiModel(
        tmdbTvShowId = TvShowTestData.Dexter.tmdbId,
        actors = TvShowCreditsTestData.Dexter.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouTvShowUiModel.Actor(imageUrl.orNull().orEmpty())
        },
        backdropUrl = TvShowTestData.Dexter.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        firstAirDate = TvShowTestData.Dexter.firstAirDate.year.toString(),
        genres = TvShowWithExtrasTestData.Dexter.tvShowWithDetails.genres.map { genre -> genre.name },
        posterUrl = TvShowTestData.Dexter.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = TvShowTestData.Dexter.rating.average.value.toString(),
        title = TvShowTestData.Dexter.title
    )

    val Grimm = ForYouTvShowUiModel(
        tmdbTvShowId = TvShowTestData.Grimm.tmdbId,
        actors = TvShowCreditsTestData.Grimm.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouTvShowUiModel.Actor(imageUrl.orNull().orEmpty())
        },
        backdropUrl = TvShowTestData.Grimm.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        firstAirDate = TvShowTestData.Grimm.firstAirDate.year.toString(),
        genres = TvShowWithExtrasTestData.Grimm.tvShowWithDetails.genres.map { genre -> genre.name },
        posterUrl = TvShowTestData.Grimm.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = TvShowTestData.Grimm.rating.average.value.toString(),
        title = TvShowTestData.Grimm.title
    )
}
