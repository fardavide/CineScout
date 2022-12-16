package cinescout.details.presentation.previewdata

import arrow.core.Option
import cinescout.common.model.CastMember
import cinescout.common.model.CrewMember
import cinescout.common.model.TmdbBackdropImage
import cinescout.common.model.TmdbPosterImage
import cinescout.common.model.TmdbProfileImage
import cinescout.details.presentation.model.MovieDetailsUiModel
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.testdata.MovieCreditsTestData
import cinescout.movies.domain.testdata.MovieMediaTestData
import cinescout.movies.domain.testdata.MovieWithExtrasTestData
import com.soywiz.klock.Date

object MovieDetailsUiModelPreviewData {

    val Inception = MovieDetailsUiModel(
        backdrops = MovieMediaTestData.Inception.backdrops.map { it.getUrl(TmdbBackdropImage.Size.ORIGINAL) },
        creditsMember = MovieCreditsTestData.Inception.members(),
        genres = MovieWithExtrasTestData.Inception.movieWithDetails.genres.map { it.name },
        isInWatchlist = MovieWithExtrasTestData.Inception.isInWatchlist,
        overview = MovieSample.Inception.overview,
        posterUrl = MovieSample.Inception.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.LARGE),
        ratings = MovieDetailsUiModel.Ratings(
            publicAverage = MovieSample.Inception.rating.average.value.toString(),
            publicCount = MovieSample.Inception.rating.voteCount.toString(),
            personal = MovieWithExtrasTestData.Inception.personalRating.fold(
                ifEmpty = { MovieDetailsUiModel.Ratings.Personal.NotRated },
                ifSome = { rating ->
                    MovieDetailsUiModel.Ratings.Personal.Rated(
                        rating = rating,
                        stringValue = rating.value.toInt().toString()
                    )
                }
            )
        ),
        releaseDate = MovieSample.Inception.releaseDate.format(),
        title = MovieSample.Inception.title,
        tmdbId = MovieSample.Inception.tmdbId,
        videos = MovieMediaTestData.Inception.videos.map { video ->
            MovieDetailsUiModel.Video(
                previewUrl = video.getPreviewUrl(),
                title = video.title,
                url = video.getVideoUrl()
            )
        }
    )

    val TheWolfOfWallStreet = MovieDetailsUiModel(
        backdrops = MovieMediaTestData.TheWolfOfWallStreet.backdrops.map { it.getUrl(TmdbBackdropImage.Size.ORIGINAL) },
        creditsMember = MovieCreditsTestData.TheWolfOfWallStreet.members(),
        genres = MovieWithExtrasTestData.TheWolfOfWallStreet.movieWithDetails.genres.map { it.name },
        isInWatchlist = MovieWithExtrasTestData.TheWolfOfWallStreet.isInWatchlist,
        overview = MovieSample.TheWolfOfWallStreet.overview,
        posterUrl = MovieSample.TheWolfOfWallStreet.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.LARGE),
        ratings = MovieDetailsUiModel.Ratings(
            publicAverage = MovieSample.TheWolfOfWallStreet.rating.average.value.toString(),
            publicCount = MovieSample.TheWolfOfWallStreet.rating.voteCount.toString(),
            personal = MovieWithExtrasTestData.TheWolfOfWallStreet.personalRating.fold(
                ifEmpty = { MovieDetailsUiModel.Ratings.Personal.NotRated },
                ifSome = { rating ->
                    MovieDetailsUiModel.Ratings.Personal.Rated(
                        rating = rating,
                        stringValue = rating.value.toInt().toString()
                    )
                }
            )
        ),
        releaseDate = MovieSample.TheWolfOfWallStreet.releaseDate.format(),
        title = MovieSample.TheWolfOfWallStreet.title,
        tmdbId = MovieSample.TheWolfOfWallStreet.tmdbId,
        videos = MovieMediaTestData.TheWolfOfWallStreet.videos.map { video ->
            MovieDetailsUiModel.Video(
                previewUrl = video.getPreviewUrl(),
                title = video.title,
                url = video.getVideoUrl()
            )
        }
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
