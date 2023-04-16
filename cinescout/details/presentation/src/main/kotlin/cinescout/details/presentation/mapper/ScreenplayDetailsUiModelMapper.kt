package cinescout.details.presentation.mapper

import cinescout.details.domain.model.ScreenplayWithExtras
import cinescout.details.presentation.model.ScreenplayDetailsUiModel
import cinescout.details.presentation.model.ScreenplayRatingsUiModel
import cinescout.media.domain.model.ScreenplayMedia
import cinescout.media.domain.model.TmdbBackdropImage
import cinescout.media.domain.model.TmdbPosterImage
import cinescout.media.domain.model.TmdbProfileImage
import cinescout.people.domain.model.CastMember
import cinescout.people.domain.model.CrewMember
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.utils.kotlin.format
import kotlinx.collections.immutable.toImmutableList
import org.koin.core.annotation.Factory

@Factory
internal class ScreenplayDetailsUiModelMapper {

    fun toUiModel(
        screenplayWithExtras: ScreenplayWithExtras,
        media: ScreenplayMedia
    ): ScreenplayDetailsUiModel {
        val screenplay = screenplayWithExtras.screenplay
        return ScreenplayDetailsUiModel(
            creditsMember = screenplayWithExtras.credits.members().toImmutableList(),
            genres = screenplayWithExtras.genres.genres.map { it.name }.toImmutableList(),
            backdrops = media.backdrops.map { it.getUrl(TmdbBackdropImage.Size.ORIGINAL) }.toImmutableList(),
            isInWatchlist = screenplayWithExtras.isInWatchlist,
            overview = screenplay.overview,
            posterUrl = media.posters.firstOrNull()?.getUrl(TmdbPosterImage.Size.LARGE),
            ratings = ScreenplayRatingsUiModel(
                publicAverage = screenplay.rating.average.value.format(digits = 1),
                publicCount = screenplay.rating.voteCount.toString(),
                personal = screenplayWithExtras.personalRating.fold(
                    ifEmpty = { ScreenplayRatingsUiModel.Personal.NotRated },
                    ifSome = { rating ->
                        ScreenplayRatingsUiModel.Personal.Rated(
                            rating = rating,
                            stringValue = rating.value.toInt().toString()
                        )
                    }
                )
            ),
            releaseDate = screenplay.relevantDate.fold(ifEmpty = { "" }, ifSome = { it.format("MMM YYYY") }),
            title = screenplay.title,
            tmdbId = screenplay.tmdbId,
            videos = media.videos.map { video ->
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
