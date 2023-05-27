package cinescout.details.presentation.mapper

import cinescout.details.domain.model.WithCredits
import cinescout.details.domain.model.WithExtra
import cinescout.details.domain.model.WithGenres
import cinescout.details.domain.model.WithMedia
import cinescout.details.domain.model.WithPersonalRating
import cinescout.details.domain.model.WithWatchlist
import cinescout.details.presentation.model.ScreenplayDetailsUiModel
import cinescout.details.presentation.model.ScreenplayRatingsUiModel
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
import cinescout.screenplay.domain.model.TvShow
import cinescout.utils.kotlin.format
import kotlinx.collections.immutable.toImmutableList
import org.koin.core.annotation.Factory

@Factory
internal class ScreenplayDetailsUiModelMapper {

    fun <T> toUiModel(
        item: T
    ): ScreenplayDetailsUiModel where T : WithExtra,
          T : WithCredits,
          T : WithGenres,
          T : WithMedia,
          T : WithPersonalRating,
          T : WithWatchlist {
        val screenplay = item.screenplay
        return ScreenplayDetailsUiModel(
            creditsMember = item.credits.members().toImmutableList(),
            genres = item.genres.genres.map { it.name }.toImmutableList(),
            backdrops = item.media.backdrops.map {
                it.getUrl(TmdbBackdropImage.Size.ORIGINAL)
            }.toImmutableList(),
            ids = item.screenplay.ids,
            isInWatchlist = item.isInWatchlist,
            overview = screenplay.overview,
            posterUrl = item.media.posters.firstOrNull()?.getUrl(TmdbPosterImage.Size.LARGE),
            ratings = ScreenplayRatingsUiModel(
                publicAverage = screenplay.rating.average.value.format(digits = 1),
                publicCount = screenplay.rating.voteCount.toString(),
                personal = item.personalRating.fold(
                    ifEmpty = { ScreenplayRatingsUiModel.Personal.NotRated },
                    ifSome = { rating ->
                        ScreenplayRatingsUiModel.Personal.Rated(
                            rating = rating,
                            stringValue = rating.value.toInt().toString()
                        )
                    }
                )
            ),
            releaseDate = screenplay.relevantDate.fold(
                ifEmpty = { "" },
                ifSome = { it.format("MMM YYYY") }
            ),
            runtime = screenplay.runtime.orNull()?.let { duration ->
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
                profileImageUrl = member.person.profileImage.orNull()?.getUrl(TmdbProfileImage.Size.SMALL),
                role = when (member) {
                    is CastMember -> member.character.orNull()
                    is CrewMember -> member.job.orNull()
                }
            )
        }
}
