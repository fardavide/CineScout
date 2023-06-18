package cinescout.details.presentation.mapper

import cinescout.details.domain.model.WithCredits
import cinescout.details.domain.model.WithGenres
import cinescout.details.domain.model.WithMedia
import cinescout.details.domain.model.WithPersonalRating
import cinescout.details.domain.model.WithScreenplay
import cinescout.details.presentation.model.ScreenplayDetailsUiModel
import cinescout.details.presentation.state.DetailsSeasonsState
import cinescout.media.domain.model.TmdbBackdropImage
import cinescout.media.domain.model.TmdbPosterImage
import cinescout.media.domain.model.TmdbProfileImage
import cinescout.people.domain.model.CastMember
import cinescout.people.domain.model.CrewMember
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.resources.R.plurals
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TvShow
import cinescout.utils.kotlin.format
import kotlinx.collections.immutable.toImmutableList
import org.koin.core.annotation.Factory

@Factory
internal class ScreenplayDetailsUiModelMapper {

    fun <T> toUiModel(
        item: T,
        seasonsState: DetailsSeasonsState
    ): ScreenplayDetailsUiModel where T : WithScreenplay,
          T : WithCredits,
          T : WithGenres,
          T : WithMedia,
          T : WithPersonalRating {
        val screenplay = item.screenplay
        return ScreenplayDetailsUiModel(
            creditsMembers = item.credits.members().toImmutableList(),
            genres = item.genres.genres.map { it.name }.toImmutableList(),
            backdrops = item.media.backdrops.map {
                it.getUrl(TmdbBackdropImage.Size.ORIGINAL)
            }.toImmutableList(),
            ids = item.screenplay.ids,
            overview = screenplay.overview,
            personalRating = item.personalRating.map(Rating::intValue).getOrNull(),
            posterUrl = item.media.posters.firstOrNull()?.getUrl(TmdbPosterImage.Size.LARGE),
            premiere = screenplay.relevantDate.fold(
                ifEmpty = { "" },
                ifSome = { it.format("MMM YYYY") }
            ),
            ratingAverage = screenplay.rating.average.value.format(digits = 1),
            ratingCount = ratingCount(screenplay.rating.voteCount),
            runtime = screenplay.runtime.getOrNull()?.let { duration ->
                when (screenplay) {
                    is Movie -> TextRes(
                        string.details_movie_runtime,
                        duration.inWholeMinutes
                    )

                    is TvShow -> TextRes.plural(
                        resId = plurals.details_tv_show_runtime,
                        quantity = screenplay.airedEpisodes,
                        screenplay.airedEpisodes,
                        duration.inWholeMinutes
                    )
                }
            },
            seasonsState = seasonsState,
            tagline = screenplay.tagline.getOrNull(),
            title = item.screenplay.title,
            videos = item.media.videos.map { video ->
                ScreenplayDetailsUiModel.Video(
                    previewUrl = video.getPreviewUrl(),
                    title = video.title,
                    url = video.getVideoUrl()
                )
            }.toImmutableList()
        )
    }

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
