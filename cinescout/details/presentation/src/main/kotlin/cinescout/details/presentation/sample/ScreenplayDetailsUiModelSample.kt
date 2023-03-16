package cinescout.details.presentation.sample

import arrow.core.Option
import cinescout.details.domain.sample.ScreenplayWithExtrasSample
import cinescout.details.presentation.model.ScreenplayDetailsUiModel
import cinescout.media.domain.model.TmdbBackdropImage
import cinescout.media.domain.model.TmdbPosterImage
import cinescout.media.domain.model.TmdbProfileImage
import cinescout.media.domain.sample.ScreenplayMediaSample
import cinescout.people.domain.model.CastMember
import cinescout.people.domain.model.CrewMember
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.people.domain.sample.ScreenplayCreditsSample
import cinescout.screenplay.domain.sample.ScreenplaySample
import com.soywiz.klock.Date
import kotlinx.collections.immutable.toImmutableList

internal object ScreenplayDetailsUiModelSample {

    val Inception = ScreenplayDetailsUiModel(
        backdrops = ScreenplayMediaSample.Inception.backdrops.map { it.getUrl(TmdbBackdropImage.Size.ORIGINAL) }
            .toImmutableList(),
        creditsMember = ScreenplayCreditsSample.Inception.members().toImmutableList(),
        genres = ScreenplayWithExtrasSample.Inception.genres.genres.map { it.name }.toImmutableList(),
        isInWatchlist = ScreenplayWithExtrasSample.Inception.isInWatchlist,
        overview = ScreenplaySample.Inception.overview,
        posterUrl = ScreenplayMediaSample.Inception.posters.firstOrNull()?.getUrl(TmdbPosterImage.Size.LARGE),
        ratings = ScreenPlayRatingsUiModelSample.Inception,
        releaseDate = ScreenplaySample.Inception.releaseDate.format(),
        title = ScreenplaySample.Inception.title,
        tmdbId = ScreenplaySample.Inception.tmdbId,
        videos = ScreenplayMediaSample.Inception.videos.map { video ->
            ScreenplayDetailsUiModel.Video(
                previewUrl = video.getPreviewUrl(),
                title = video.title,
                url = video.getVideoUrl()
            )
        }.toImmutableList()
    )

    val TheWolfOfWallStreet = ScreenplayDetailsUiModel(
        backdrops = ScreenplayMediaSample.TheWolfOfWallStreet.backdrops
            .map { it.getUrl(TmdbBackdropImage.Size.ORIGINAL) }
            .toImmutableList(),
        creditsMember = ScreenplayCreditsSample.TheWolfOfWallStreet.members().toImmutableList(),
        genres = ScreenplayWithExtrasSample.TheWolfOfWallStreet.genres.genres.map { it.name }.toImmutableList(),
        isInWatchlist = ScreenplayWithExtrasSample.TheWolfOfWallStreet.isInWatchlist,
        overview = ScreenplaySample.TheWolfOfWallStreet.overview,
        posterUrl = ScreenplayMediaSample.TheWolfOfWallStreet.posters.firstOrNull()?.getUrl(TmdbPosterImage.Size.LARGE),
        ratings = ScreenPlayRatingsUiModelSample.TheWolfOfWallStreet,
        releaseDate = ScreenplaySample.TheWolfOfWallStreet.releaseDate.format(),
        title = ScreenplaySample.TheWolfOfWallStreet.title,
        tmdbId = ScreenplaySample.TheWolfOfWallStreet.tmdbId,
        videos = ScreenplayMediaSample.TheWolfOfWallStreet.videos.map { video ->
            ScreenplayDetailsUiModel.Video(
                previewUrl = video.getPreviewUrl(),
                title = video.title,
                url = video.getVideoUrl()
            )
        }.toImmutableList()
    )

    private fun Option<Date>.format() = fold(ifEmpty = { "" }, ifSome = { it.format("MMM YYYY") })

    private fun ScreenplayCredits.members(): List<ScreenplayDetailsUiModel.CreditsMember> =
        (cast + crew).map { member ->
            ScreenplayDetailsUiModel.CreditsMember(
                name = member.person.name,
                profileImageUrl = member.person.profileImage.orNull()?.getUrl(TmdbProfileImage.Size.SMALL),
                role = when (member) {
                    is CastMember -> member.character.orNull()
                    is CrewMember -> member.job.orNull()
                }
            )
        }
}
