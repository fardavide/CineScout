package cinescout.details.presentation.mapper

import cinescout.common.model.*
import cinescout.details.presentation.model.TvShowDetailsUiModel
import cinescout.tvshows.domain.model.TvShowCredits
import cinescout.tvshows.domain.model.TvShowMedia
import cinescout.tvshows.domain.model.TvShowWithExtras

class TvShowDetailsUiModelMapper {

    fun toUiModel(tvShowWithExtras: TvShowWithExtras, media: TvShowMedia): TvShowDetailsUiModel {
        val tvShow = tvShowWithExtras.tvShowWithDetails.tvShow
        return TvShowDetailsUiModel(
            creditsMember = tvShowWithExtras.credits.members(),
            genres = tvShowWithExtras.tvShowWithDetails.genres.map { it.name },
            backdrops = (listOfNotNull(tvShow.backdropImage.orNull()) + media.backdrops).distinct()
                .map { it.getUrl(TmdbBackdropImage.Size.ORIGINAL) },
            firstAirDate = tvShow.firstAirDate.format("MMM YYYY"),
            isInWatchlist = tvShowWithExtras.isInWatchlist,
            overview = tvShow.overview,
            posterUrl = tvShow.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.LARGE),
            ratings = TvShowDetailsUiModel.Ratings(
                publicAverage = tvShow.rating.average.value.toString(),
                publicCount = tvShow.rating.voteCount.toString(),
                personal = tvShowWithExtras.personalRating.fold(
                    ifEmpty = { TvShowDetailsUiModel.Ratings.Personal.NotRated },
                    ifSome = { rating ->
                        TvShowDetailsUiModel.Ratings.Personal.Rated(
                            rating = rating,
                            stringValue = rating.value.toInt().toString()
                        )
                    }
                )
            ),
            title = tvShow.title,
            tmdbId = tvShow.tmdbId,
            videos = media.videos.map { video ->
                TvShowDetailsUiModel.Video(
                    previewUrl = video.getPreviewUrl(),
                    title = video.title,
                    url = video.getVideoUrl()
                )
            }
        )
    }

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
