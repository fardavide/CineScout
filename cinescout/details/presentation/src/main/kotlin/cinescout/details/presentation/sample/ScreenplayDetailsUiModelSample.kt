package cinescout.details.presentation.sample

import arrow.core.Option
import cinescout.details.presentation.model.ScreenplayDetailsUiModel
import cinescout.media.domain.model.TmdbBackdropImage
import cinescout.media.domain.model.TmdbPosterImage
import cinescout.media.domain.model.TmdbProfileImage
import cinescout.media.domain.sample.ScreenplayMediaSample
import cinescout.people.domain.model.CastMember
import cinescout.people.domain.model.CrewMember
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.people.domain.sample.ScreenplayCreditsSample
import cinescout.rating.domain.sample.ScreenplayPersonalRatingSample
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.screenplay.domain.sample.ScreenplayGenresSample
import cinescout.screenplay.domain.sample.ScreenplayIdsSample
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.utils.kotlin.format
import korlibs.time.Date
import kotlinx.collections.immutable.toImmutableList

internal object ScreenplayDetailsUiModelSample {

    val Inception = ScreenplayDetailsUiModel(
        backdrops = ScreenplayMediaSample.Inception.backdrops.map {
            it.getUrl(TmdbBackdropImage.Size.ORIGINAL)
        }
            .toImmutableList(),
        creditsMember = ScreenplayCreditsSample.Inception.members().toImmutableList(),
        genres = ScreenplayGenresSample.Inception.genres.map { it.name }.toImmutableList(),
        ids = ScreenplayIdsSample.Inception,
        overview = ScreenplaySample.Inception.overview,
        personalRating = ScreenplayPersonalRatingSample.Inception.map { it.intValue }.toOption(),
        posterUrl = ScreenplayMediaSample.Inception.posters.firstOrNull()?.getUrl(TmdbPosterImage.Size.LARGE),
        releaseDate = ScreenplaySample.Inception.releaseDate.format(),
        ratingAverage = ScreenplaySample.Inception.rating.average.value.format(digits = 1),
        ratingCount = ScreenplaySample.Inception.rating.voteCount.toString(),
        runtime = ScreenplaySample.Inception.runtime.orNull()
            ?.let { TextRes(string.details_movie_runtime, it.inWholeMinutes) },
        title = ScreenplaySample.Inception.title,
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
        genres = ScreenplayGenresSample.TheWolfOfWallStreet.genres.map { it.name }.toImmutableList(),
        ids = ScreenplayIdsSample.TheWolfOfWallStreet,
        overview = ScreenplaySample.TheWolfOfWallStreet.overview,
        personalRating = ScreenplayPersonalRatingSample.TheWolfOfWallStreet.map { it.intValue }.toOption(),
        posterUrl = ScreenplayMediaSample.TheWolfOfWallStreet.posters.firstOrNull()?.getUrl(TmdbPosterImage.Size.LARGE),
        ratingAverage = ScreenplaySample.TheWolfOfWallStreet.rating.average.value.format(digits = 1),
        ratingCount = ScreenplaySample.TheWolfOfWallStreet.rating.voteCount.toString(),
        releaseDate = ScreenplaySample.TheWolfOfWallStreet.releaseDate.format(),
        runtime = ScreenplaySample.TheWolfOfWallStreet.runtime.orNull()
            ?.let { TextRes(string.details_movie_runtime, it.inWholeMinutes) },
        title = ScreenplaySample.TheWolfOfWallStreet.title,
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
