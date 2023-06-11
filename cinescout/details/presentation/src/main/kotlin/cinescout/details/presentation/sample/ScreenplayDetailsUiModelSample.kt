package cinescout.details.presentation.sample

import arrow.core.Option
import cinescout.details.presentation.model.ScreenplayDetailsUiModel
import cinescout.details.presentation.state.DetailsSeasonsState
import cinescout.media.domain.model.TmdbBackdropImage
import cinescout.media.domain.model.TmdbPosterImage
import cinescout.media.domain.model.TmdbProfileImage
import cinescout.media.domain.sample.ScreenplayMediaSample
import cinescout.people.domain.model.CastMember
import cinescout.people.domain.model.CrewMember
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.people.domain.sample.ScreenplayCreditsSample
import cinescout.rating.domain.sample.ScreenplayPersonalRatingSample
import cinescout.resources.R.plurals
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.screenplay.domain.model.Rating
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
        creditsMembers = ScreenplayCreditsSample.Inception.members().toImmutableList(),
        genres = ScreenplayGenresSample.Inception.genres.map { it.name }.toImmutableList(),
        ids = ScreenplayIdsSample.Inception,
        overview = ScreenplaySample.Inception.overview,
        personalRating = ScreenplayPersonalRatingSample.Inception.map(Rating::intValue).getOrNull(),
        posterUrl = ScreenplayMediaSample.Inception.posters.firstOrNull()?.getUrl(TmdbPosterImage.Size.LARGE),
        releaseDate = ScreenplaySample.Inception.releaseDate.format(),
        ratingAverage = ScreenplaySample.Inception.rating.average.value.format(digits = 1),
        ratingCount = ratingCount(ScreenplaySample.Inception.rating.voteCount),
        runtime = ScreenplaySample.Inception.runtime.getOrNull()
            ?.let { TextRes(string.details_movie_runtime, it.inWholeMinutes) },
        seasonsState = DetailsSeasonsState.NoSeasons,
        tagline = ScreenplaySample.Inception.tagline.getOrNull(),
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
        creditsMembers = ScreenplayCreditsSample.TheWolfOfWallStreet.members().toImmutableList(),
        genres = ScreenplayGenresSample.TheWolfOfWallStreet.genres.map { it.name }.toImmutableList(),
        ids = ScreenplayIdsSample.TheWolfOfWallStreet,
        overview = ScreenplaySample.TheWolfOfWallStreet.overview,
        personalRating = ScreenplayPersonalRatingSample.TheWolfOfWallStreet.map(Rating::intValue).getOrNull(),
        posterUrl = ScreenplayMediaSample.TheWolfOfWallStreet.posters.firstOrNull()?.getUrl(TmdbPosterImage.Size.LARGE),
        ratingAverage = ScreenplaySample.TheWolfOfWallStreet.rating.average.value.format(digits = 1),
        ratingCount = ratingCount(ScreenplaySample.TheWolfOfWallStreet.rating.voteCount),
        releaseDate = ScreenplaySample.TheWolfOfWallStreet.releaseDate.format(),
        runtime = ScreenplaySample.TheWolfOfWallStreet.runtime.getOrNull()
            ?.let { TextRes(string.details_movie_runtime, it.inWholeMinutes) },
        seasonsState = DetailsSeasonsState.NoSeasons,
        tagline = ScreenplaySample.TheWolfOfWallStreet.tagline.getOrNull(),
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
                profileImageUrl = member.person.profileImage.getOrNull()?.getUrl(TmdbProfileImage.Size.SMALL),
                role = when (member) {
                    is CastMember -> member.character.getOrNull()
                    is CrewMember -> member.job.getOrNull()
                }
            )
        }

    private fun ratingCount(count: Int): TextRes = if (count > 1_000) {
        TextRes(string.details_votes_k, (count.toDouble() / 1000).format(digits = 1))
    } else {
        TextRes.plural(plurals.details_votes, quantity = count, count)
    }
}
