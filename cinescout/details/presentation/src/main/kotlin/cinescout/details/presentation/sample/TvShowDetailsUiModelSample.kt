package cinescout.details.presentation.sample

import cinescout.details.presentation.model.TvShowDetailsUiModel
import cinescout.screenplay.domain.model.CastMember
import cinescout.screenplay.domain.model.CrewMember
import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage
import cinescout.screenplay.domain.model.TmdbProfileImage
import cinescout.tvshows.domain.model.TvShowCredits
import cinescout.tvshows.domain.sample.TvShowCreditsSample
import cinescout.tvshows.domain.sample.TvShowSample
import cinescout.tvshows.domain.sample.TvShowWithExtrasSample
import cinescout.tvshows.domain.testdata.TvShowMediaTestData
import com.soywiz.klock.Date
import kotlinx.collections.immutable.toImmutableList

object TvShowDetailsUiModelSample {

    val BreakingBad = TvShowDetailsUiModel(
        backdrops = TvShowMediaTestData.BreakingBad.backdrops.map { it.getUrl(TmdbBackdropImage.Size.ORIGINAL) }
            .toImmutableList(),
        creditsMember = TvShowCreditsSample.BreakingBad.members().toImmutableList(),
        genres = TvShowWithExtrasSample.BreakingBad.tvShowWithDetails.genres.map { it.name }.toImmutableList(),
        isInWatchlist = TvShowWithExtrasSample.BreakingBad.isInWatchlist,
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
        creditsMember = TvShowCreditsSample.Grimm.members().toImmutableList(),
        genres = TvShowWithExtrasSample.Grimm.tvShowWithDetails.genres.map { it.name }.toImmutableList(),
        isInWatchlist = TvShowWithExtrasSample.Grimm.isInWatchlist,
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