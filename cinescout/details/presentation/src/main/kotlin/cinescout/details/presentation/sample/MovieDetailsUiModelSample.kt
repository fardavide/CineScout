package cinescout.details.presentation.sample

import arrow.core.Option
import cinescout.details.presentation.model.MovieDetailsUiModel
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.sample.MovieCreditsSample
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.sample.MovieWithExtrasSample
import cinescout.movies.domain.testdata.MovieMediaTestData
import cinescout.screenplay.domain.model.CastMember
import cinescout.screenplay.domain.model.CrewMember
import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage
import cinescout.screenplay.domain.model.TmdbProfileImage
import com.soywiz.klock.Date
import kotlinx.collections.immutable.toImmutableList

internal object MovieDetailsUiModelSample {

    val Inception = MovieDetailsUiModel(
        backdrops = MovieMediaTestData.Inception.backdrops.map { it.getUrl(TmdbBackdropImage.Size.ORIGINAL) }
            .toImmutableList(),
        creditsMember = MovieCreditsSample.Inception.members().toImmutableList(),
        genres = MovieWithExtrasSample.Inception.movieWithDetails.genres.map { it.name }.toImmutableList(),
        isInWatchlist = MovieWithExtrasSample.Inception.isInWatchlist,
        overview = MovieSample.Inception.overview,
        posterUrl = MovieSample.Inception.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.LARGE),
        ratings = ScreenPlayRatingsUiModelSample.Inception,
        releaseDate = MovieSample.Inception.releaseDate.format(),
        title = MovieSample.Inception.title,
        tmdbId = MovieSample.Inception.tmdbId,
        videos = MovieMediaTestData.Inception.videos.map { video ->
            MovieDetailsUiModel.Video(
                previewUrl = video.getPreviewUrl(),
                title = video.title,
                url = video.getVideoUrl()
            )
        }.toImmutableList()
    )

    val TheWolfOfWallStreet = MovieDetailsUiModel(
        backdrops = MovieMediaTestData.TheWolfOfWallStreet.backdrops.map { it.getUrl(TmdbBackdropImage.Size.ORIGINAL) }
            .toImmutableList(),
        creditsMember = MovieCreditsSample.TheWolfOfWallStreet.members().toImmutableList(),
        genres = MovieWithExtrasSample.TheWolfOfWallStreet.movieWithDetails.genres.map { it.name }.toImmutableList(),
        isInWatchlist = MovieWithExtrasSample.TheWolfOfWallStreet.isInWatchlist,
        overview = MovieSample.TheWolfOfWallStreet.overview,
        posterUrl = MovieSample.TheWolfOfWallStreet.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.LARGE),
        ratings = ScreenPlayRatingsUiModelSample.TheWolfOfWallStreet,
        releaseDate = MovieSample.TheWolfOfWallStreet.releaseDate.format(),
        title = MovieSample.TheWolfOfWallStreet.title,
        tmdbId = MovieSample.TheWolfOfWallStreet.tmdbId,
        videos = MovieMediaTestData.TheWolfOfWallStreet.videos.map { video ->
            MovieDetailsUiModel.Video(
                previewUrl = video.getPreviewUrl(),
                title = video.title,
                url = video.getVideoUrl()
            )
        }.toImmutableList()
    )

    private fun Option<Date>.format() = fold(ifEmpty = { "" }, ifSome = { it.format("MMM YYYY") })

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