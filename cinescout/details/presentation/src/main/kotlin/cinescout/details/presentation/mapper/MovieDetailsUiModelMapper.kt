package cinescout.details.presentation.mapper

import cinescout.details.presentation.model.MovieDetailsUiModel
import cinescout.details.presentation.model.ScreenPlayRatingsUiModel
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieMedia
import cinescout.movies.domain.model.MovieWithExtras
import cinescout.screenplay.domain.model.CastMember
import cinescout.screenplay.domain.model.CrewMember
import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage
import cinescout.screenplay.domain.model.TmdbProfileImage
import kotlinx.collections.immutable.toImmutableList
import org.koin.core.annotation.Factory

@Factory
internal class MovieDetailsUiModelMapper {

    fun toUiModel(movieWithExtras: MovieWithExtras, media: MovieMedia): MovieDetailsUiModel {
        val movie = movieWithExtras.movieWithDetails.movie
        return MovieDetailsUiModel(
            creditsMember = movieWithExtras.credits.members().toImmutableList(),
            genres = movieWithExtras.movieWithDetails.genres.map { it.name }.toImmutableList(),
            backdrops = (listOfNotNull(movie.backdropImage.orNull()) + media.backdrops).distinct()
                .map { it.getUrl(TmdbBackdropImage.Size.ORIGINAL) }
                .toImmutableList(),
            isInWatchlist = movieWithExtras.isInWatchlist,
            overview = movieWithExtras.movieWithDetails.movie.overview,
            posterUrl = movie.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.LARGE),
            ratings = ScreenPlayRatingsUiModel(
                publicAverage = movie.rating.average.value.toString(),
                publicCount = movie.rating.voteCount.toString(),
                personal = movieWithExtras.personalRating.fold(
                    ifEmpty = { ScreenPlayRatingsUiModel.Personal.NotRated },
                    ifSome = { rating ->
                        ScreenPlayRatingsUiModel.Personal.Rated(
                            rating = rating,
                            stringValue = rating.value.toInt().toString()
                        )
                    }
                )
            ),
            releaseDate = movie.releaseDate.fold(ifEmpty = { "" }, ifSome = { it.format("MMM YYYY") }),
            title = movie.title,
            tmdbId = movie.tmdbId,
            videos = media.videos.map { video ->
                MovieDetailsUiModel.Video(
                    previewUrl = video.getPreviewUrl(),
                    title = video.title,
                    url = video.getVideoUrl()
                )
            }.toImmutableList()
        )
    }

    private fun MovieCredits.members(): List<MovieDetailsUiModel.CreditsMember> =
        (cast + crew).map { member ->
            MovieDetailsUiModel.CreditsMember(
                name = member.person.name,
                profileImageUrl = member.person.profileImage.orNull()?.getUrl(TmdbProfileImage.Size.SMALL),
                role = when (member) {
                    is CastMember -> member.character.orNull()
                    is CrewMember -> member.job.orNull()
                }
            )
        }
}
