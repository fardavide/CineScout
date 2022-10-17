package cinescout.details.presentation.previewdata

import arrow.core.Option
import cinescout.common.model.*
import cinescout.details.presentation.model.MovieDetailsUiModel
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.testdata.MovieCreditsTestData
import cinescout.movies.domain.testdata.MovieMediaTestData
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithExtrasTestData
import com.soywiz.klock.Date

object MovieDetailsUiModelPreviewData {

    val Inception = MovieDetailsUiModel(
        backdrops = MovieMediaTestData.Inception.backdrops.map { it.getUrl(TmdbBackdropImage.Size.ORIGINAL) },
        creditsMember = MovieCreditsTestData.Inception.members(),
        genres = MovieWithExtrasTestData.Inception.movieWithDetails.genres.map { it.name },
        isInWatchlist = MovieWithExtrasTestData.Inception.isInWatchlist,
        overview = MovieTestData.Inception.overview,
        posterUrl = MovieTestData.Inception.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.LARGE),
        ratings = MovieDetailsUiModel.Ratings(
            publicAverage = MovieTestData.Inception.rating.average.value.toString(),
            publicCount = MovieTestData.Inception.rating.voteCount.toString(),
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
        releaseDate = MovieTestData.Inception.releaseDate.format(),
        title = MovieTestData.Inception.title,
        tmdbId = MovieTestData.Inception.tmdbId,
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
        overview = MovieTestData.TheWolfOfWallStreet.overview,
        posterUrl = MovieTestData.TheWolfOfWallStreet.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.LARGE),
        ratings = MovieDetailsUiModel.Ratings(
            publicAverage = MovieTestData.TheWolfOfWallStreet.rating.average.value.toString(),
            publicCount = MovieTestData.TheWolfOfWallStreet.rating.voteCount.toString(),
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
        releaseDate = MovieTestData.TheWolfOfWallStreet.releaseDate.format(),
        title = MovieTestData.TheWolfOfWallStreet.title,
        tmdbId = MovieTestData.TheWolfOfWallStreet.tmdbId,
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
