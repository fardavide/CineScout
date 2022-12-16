package cinescout.details.presentation.previewdata

import cinescout.common.model.*
import cinescout.details.presentation.model.TvShowDetailsUiModel
import cinescout.tvshows.domain.model.TvShowCredits
import cinescout.tvshows.domain.sample.TvShowSample
import cinescout.tvshows.domain.testdata.TvShowCreditsTestData
import cinescout.tvshows.domain.testdata.TvShowMediaTestData
import cinescout.tvshows.domain.testdata.TvShowWithExtrasTestData
import com.soywiz.klock.Date

object TvShowDetailsUiModelPreviewData {

    val BreakingBad = TvShowDetailsUiModel(
        backdrops = TvShowMediaTestData.BreakingBad.backdrops.map { it.getUrl(TmdbBackdropImage.Size.ORIGINAL) },
        creditsMember = TvShowCreditsTestData.BreakingBad.members(),
        genres = TvShowWithExtrasTestData.BreakingBad.tvShowWithDetails.genres.map { it.name },
        isInWatchlist = TvShowWithExtrasTestData.BreakingBad.isInWatchlist,
        firstAirDate = TvShowSample.BreakingBad.firstAirDate.format(),
        overview = TvShowSample.BreakingBad.overview,
        posterUrl = TvShowSample.BreakingBad.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.LARGE),
        ratings = TvShowDetailsUiModel.Ratings(
            publicAverage = TvShowSample.BreakingBad.rating.average.value.toString(),
            publicCount = TvShowSample.BreakingBad.rating.voteCount.toString(),
            personal = TvShowWithExtrasTestData.BreakingBad.personalRating.fold(
                ifEmpty = { TvShowDetailsUiModel.Ratings.Personal.NotRated },
                ifSome = { rating ->
                    TvShowDetailsUiModel.Ratings.Personal.Rated(
                        rating = rating,
                        stringValue = rating.value.toInt().toString()
                    )
                }
            )
        ),
        title = TvShowSample.BreakingBad.title,
        tmdbId = TvShowSample.BreakingBad.tmdbId,
        videos = TvShowMediaTestData.BreakingBad.videos.map { video ->
            TvShowDetailsUiModel.Video(
                previewUrl = video.getPreviewUrl(),
                title = video.title,
                url = video.getVideoUrl()
            )
        }
    )

    val Grimm = TvShowDetailsUiModel(
        backdrops = TvShowMediaTestData.Grimm.backdrops.map { it.getUrl(TmdbBackdropImage.Size.ORIGINAL) },
        creditsMember = TvShowCreditsTestData.Grimm.members(),
        genres = TvShowWithExtrasTestData.Grimm.tvShowWithDetails.genres.map { it.name },
        isInWatchlist = TvShowWithExtrasTestData.Grimm.isInWatchlist,
        firstAirDate = TvShowSample.Grimm.firstAirDate.format(),
        overview = TvShowSample.Grimm.overview,
        posterUrl = TvShowSample.Grimm.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.LARGE),
        ratings = TvShowDetailsUiModel.Ratings(
            publicAverage = TvShowSample.Grimm.rating.average.value.toString(),
            publicCount = TvShowSample.Grimm.rating.voteCount.toString(),
            personal = TvShowWithExtrasTestData.Grimm.personalRating.fold(
                ifEmpty = { TvShowDetailsUiModel.Ratings.Personal.NotRated },
                ifSome = { rating ->
                    TvShowDetailsUiModel.Ratings.Personal.Rated(
                        rating = rating,
                        stringValue = rating.value.toInt().toString()
                    )
                }
            )
        ),
        title = TvShowSample.Grimm.title,
        tmdbId = TvShowSample.Grimm.tmdbId,
        videos = TvShowMediaTestData.Grimm.videos.map { video ->
            TvShowDetailsUiModel.Video(
                previewUrl = video.getPreviewUrl(),
                title = video.title,
                url = video.getVideoUrl()
            )
        }
    )

    private fun Date.format() = format("MMM YYYY")
    private fun TvShowCredits.members(): List<TvShowDetailsUiModel.CreditsMember> =
        (cast + crew).map { member ->
            TvShowDetailsUiModel.CreditsMember(
                name = member.person.name,
                profileImageUrl = member.person.profileImage.orNull()?.getUrl(TmdbProfileImage.Size.SMALL),
                role = when (member) {
                    is CastMember -> member.character.orNull()
                    is CrewMember -> member.job.orNull()
                }
            )
        }
}
