package cinescout.suggestions.presentation.sample

import cinescout.common.model.TmdbBackdropImage
import cinescout.common.model.TmdbPosterImage
import cinescout.common.model.TmdbProfileImage
import cinescout.suggestions.presentation.model.ForYouTvShowUiModel
import cinescout.tvshows.domain.sample.TvShowSample
import cinescout.tvshows.domain.testdata.TvShowCreditsTestData
import cinescout.tvshows.domain.testdata.TvShowWithExtrasTestData

object ForYouTvShowUiModelSample {

    val BreakingBad = ForYouTvShowUiModel(
        tmdbTvShowId = TvShowSample.BreakingBad.tmdbId,
        actors = TvShowCreditsTestData.BreakingBad.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouTvShowUiModel.Actor(imageUrl.orNull().orEmpty())
        },
        backdropUrl = TvShowSample.BreakingBad.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        firstAirDate = TvShowSample.BreakingBad.firstAirDate.year.toString(),
        genres = TvShowWithExtrasTestData.BreakingBad.tvShowWithDetails.genres.map { genre -> genre.name },
        posterUrl = TvShowSample.BreakingBad.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = TvShowSample.BreakingBad.rating.average.value.toString(),
        title = TvShowSample.BreakingBad.title
    )

    val Dexter = ForYouTvShowUiModel(
        tmdbTvShowId = TvShowSample.Dexter.tmdbId,
        actors = TvShowCreditsTestData.Dexter.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouTvShowUiModel.Actor(imageUrl.orNull().orEmpty())
        },
        backdropUrl = TvShowSample.Dexter.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        firstAirDate = TvShowSample.Dexter.firstAirDate.year.toString(),
        genres = TvShowWithExtrasTestData.Dexter.tvShowWithDetails.genres.map { genre -> genre.name },
        posterUrl = TvShowSample.Dexter.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = TvShowSample.Dexter.rating.average.value.toString(),
        title = TvShowSample.Dexter.title
    )

    val Grimm = ForYouTvShowUiModel(
        tmdbTvShowId = TvShowSample.Grimm.tmdbId,
        actors = TvShowCreditsTestData.Grimm.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouTvShowUiModel.Actor(imageUrl.orNull().orEmpty())
        },
        backdropUrl = TvShowSample.Grimm.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        firstAirDate = TvShowSample.Grimm.firstAirDate.year.toString(),
        genres = TvShowWithExtrasTestData.Grimm.tvShowWithDetails.genres.map { genre -> genre.name },
        posterUrl = TvShowSample.Grimm.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = TvShowSample.Grimm.rating.average.value.toString(),
        title = TvShowSample.Grimm.title
    )
}
