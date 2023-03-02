package cinescout.suggestions.presentation.sample

import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage
import cinescout.screenplay.domain.model.TmdbProfileImage
import cinescout.suggestions.presentation.model.ForYouScreenplayUiModel
import cinescout.tvshows.domain.sample.TvShowSample
import cinescout.tvshows.domain.testdata.TvShowCreditsTestData
import cinescout.tvshows.domain.testdata.TvShowWithExtrasTestData
import kotlinx.collections.immutable.toImmutableList

object ForYouTvShowUiModelSample {

    val BreakingBad = ForYouScreenplayUiModel(
        actors = TvShowCreditsTestData.BreakingBad.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouScreenplayUiModel.Actor(imageUrl.orNull().orEmpty())
        }.toImmutableList(),
        backdropUrl = TvShowSample.BreakingBad.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        genres = TvShowWithExtrasTestData.BreakingBad.tvShowWithDetails.genres.map { genre -> genre.name }
            .toImmutableList(),
        posterUrl = TvShowSample.BreakingBad.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = TvShowSample.BreakingBad.rating.average.value.toString(),
        releaseYear = TvShowSample.BreakingBad.firstAirDate.year.toString(),
        title = TvShowSample.BreakingBad.title,
        tmdbScreenplayId = TvShowSample.BreakingBad.tmdbId
    )

    val Dexter = ForYouScreenplayUiModel(
        actors = TvShowCreditsTestData.Dexter.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouScreenplayUiModel.Actor(imageUrl.orNull().orEmpty())
        }.toImmutableList(),
        backdropUrl = TvShowSample.Dexter.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        genres = TvShowWithExtrasTestData.Dexter.tvShowWithDetails.genres.map { genre -> genre.name }.toImmutableList(),
        posterUrl = TvShowSample.Dexter.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = TvShowSample.Dexter.rating.average.value.toString(),
        releaseYear = TvShowSample.Dexter.firstAirDate.year.toString(),
        title = TvShowSample.Dexter.title,
        tmdbScreenplayId = TvShowSample.Dexter.tmdbId
    )

    val Grimm = ForYouScreenplayUiModel(
        actors = TvShowCreditsTestData.Grimm.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouScreenplayUiModel.Actor(imageUrl.orNull().orEmpty())
        }.toImmutableList(),
        backdropUrl = TvShowSample.Grimm.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        genres = TvShowWithExtrasTestData.Grimm.tvShowWithDetails.genres.map { genre -> genre.name }.toImmutableList(),
        posterUrl = TvShowSample.Grimm.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = TvShowSample.Grimm.rating.average.value.toString(),
        releaseYear = TvShowSample.Grimm.firstAirDate.year.toString(),
        title = TvShowSample.Grimm.title,
        tmdbScreenplayId = TvShowSample.Grimm.tmdbId
    )
}
