package cinescout.details.presentation.sample

import cinescout.common.model.CastMember
import cinescout.common.model.CrewMember
import cinescout.common.model.TmdbBackdropImage
import cinescout.common.model.TmdbPosterImage
import cinescout.common.model.TmdbProfileImage
import cinescout.details.presentation.model.TvShowDetailsUiModel
import cinescout.tvshows.domain.model.TvShowCredits
import cinescout.tvshows.domain.sample.TvShowSample
import cinescout.tvshows.domain.testdata.TvShowCreditsTestData
import cinescout.tvshows.domain.testdata.TvShowMediaTestData
import cinescout.tvshows.domain.testdata.TvShowWithExtrasTestData
import com.soywiz.klock.Date
import kotlinx.collections.immutable.toImmutableList

object TvShowDetailsUiModelSample {

    val BreakingBad = TvShowDetailsUiModel(
        backdrops = TvShowMediaTestData.BreakingBad.backdrops.map { it.getUrl(TmdbBackdropImage.Size.ORIGINAL) }
            .toImmutableList(),
        creditsMember = TvShowCreditsTestData.BreakingBad.members().toImmutableList(),
        genres = TvShowWithExtrasTestData.BreakingBad.tvShowWithDetails.genres.map { it.name }.toImmutableList(),
        isInWatchlist = TvShowWithExtrasTestData.BreakingBad.isInWatchlist,
        firstAirDate = TvShowSample.BreakingBad.firstAirDate.format(),
        overview = TvShowSample.BreakingBad.overview,
        posterUrl = TvShowSample.BreakingBad.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.LARGE),
        ratings = ScreenPlayRatingsUiModelSample.BreakingBad,
        title = TvShowSample.BreakingBad.title,
        tmdbId = TvShowSample.BreakingBad.tmdbId,
        videos = TvShowMediaTestData.BreakingBad.videos.map { video ->
            TvShowDetailsUiModel.Video(
                previewUrl = video.getPreviewUrl(),
                title = video.title,
                url = video.getVideoUrl()
            )
        }.toImmutableList()
    )

    val Grimm = TvShowDetailsUiModel(
        backdrops = TvShowMediaTestData.Grimm.backdrops.map { it.getUrl(TmdbBackdropImage.Size.ORIGINAL) }
            .toImmutableList(),
        creditsMember = TvShowCreditsTestData.Grimm.members().toImmutableList(),
        genres = TvShowWithExtrasTestData.Grimm.tvShowWithDetails.genres.map { it.name }.toImmutableList(),
        isInWatchlist = TvShowWithExtrasTestData.Grimm.isInWatchlist,
        firstAirDate = TvShowSample.Grimm.firstAirDate.format(),
        overview = TvShowSample.Grimm.overview,
        posterUrl = TvShowSample.Grimm.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.LARGE),
        ratings = ScreenPlayRatingsUiModelSample.Grimm,
        title = TvShowSample.Grimm.title,
        tmdbId = TvShowSample.Grimm.tmdbId,
        videos = TvShowMediaTestData.Grimm.videos.map { video ->
            TvShowDetailsUiModel.Video(
                previewUrl = video.getPreviewUrl(),
                title = video.title,
                url = video.getVideoUrl()
            )
        }.toImmutableList()
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
