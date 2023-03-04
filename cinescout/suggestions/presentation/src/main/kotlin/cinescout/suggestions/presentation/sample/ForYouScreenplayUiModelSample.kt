package cinescout.suggestions.presentation.sample

import cinescout.design.R.string
import cinescout.design.TextRes
import cinescout.movies.domain.sample.MovieCreditsSample
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.sample.MovieWithExtrasSample
import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage
import cinescout.screenplay.domain.model.TmdbProfileImage
import cinescout.suggestions.domain.sample.SuggestedMovieSample
import cinescout.suggestions.domain.sample.SuggestedTvShowSample
import cinescout.suggestions.presentation.model.ForYouScreenplayUiModel
import cinescout.tvshows.domain.sample.TvShowCreditsSample
import cinescout.tvshows.domain.sample.TvShowSample
import cinescout.tvshows.domain.sample.TvShowWithExtrasSample
import kotlinx.collections.immutable.toImmutableList

object ForYouScreenplayUiModelSample {

    val BreakingBad = ForYouScreenplayUiModel(
        tmdbScreenplayId = TvShowSample.BreakingBad.tmdbId,
        actors = TvShowCreditsSample.BreakingBad.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouScreenplayUiModel.Actor(imageUrl.orNull().orEmpty())
        }.toImmutableList(),
        affinity = SuggestedTvShowSample.BreakingBad.affinity.value,
        backdropUrl = TvShowSample.BreakingBad.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        genres = TvShowWithExtrasSample.BreakingBad.tvShowWithDetails.genres.map { genre -> genre.name }
            .toImmutableList(),
        posterUrl = TvShowSample.BreakingBad.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = TvShowSample.BreakingBad.rating.average.value.toString(),
        releaseYear = TvShowSample.BreakingBad.firstAirDate.year.toString(),
        suggestionSource = TextRes(string.suggestions_source_popular),
        title = TvShowSample.BreakingBad.title
    )

    val Dexter = ForYouScreenplayUiModel(
        actors = TvShowCreditsSample.Dexter.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouScreenplayUiModel.Actor(imageUrl.orNull().orEmpty())
        }.toImmutableList(),
        affinity = SuggestedTvShowSample.Dexter.affinity.value,
        backdropUrl = TvShowSample.Dexter.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        genres = TvShowWithExtrasSample.Dexter.tvShowWithDetails.genres.map { genre -> genre.name }.toImmutableList(),
        posterUrl = TvShowSample.Dexter.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = TvShowSample.Dexter.rating.average.value.toString(),
        releaseYear = TvShowSample.Dexter.firstAirDate.year.toString(),
        suggestionSource = TextRes(string.suggestions_source_popular),
        title = TvShowSample.Dexter.title,
        tmdbScreenplayId = TvShowSample.Dexter.tmdbId
    )

    val Grimm = ForYouScreenplayUiModel(
        actors = TvShowCreditsSample.Grimm.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouScreenplayUiModel.Actor(imageUrl.orNull().orEmpty())
        }.toImmutableList(),
        affinity = SuggestedTvShowSample.Grimm.affinity.value,
        backdropUrl = TvShowSample.Grimm.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        genres = TvShowWithExtrasSample.Grimm.tvShowWithDetails.genres.map { genre -> genre.name }.toImmutableList(),
        posterUrl = TvShowSample.Grimm.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = TvShowSample.Grimm.rating.average.value.toString(),
        releaseYear = TvShowSample.Grimm.firstAirDate.year.toString(),
        suggestionSource = TextRes(string.suggestions_source_popular),
        title = TvShowSample.Grimm.title,
        tmdbScreenplayId = TvShowSample.Grimm.tmdbId
    )

    val Inception = ForYouScreenplayUiModel(
        actors = MovieCreditsSample.Inception.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouScreenplayUiModel.Actor(imageUrl.orNull().orEmpty())
        }.toImmutableList(),
        affinity = SuggestedMovieSample.Inception.affinity.value,
        backdropUrl = MovieSample.Inception.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        genres = MovieWithExtrasSample.Inception.movieWithDetails.genres.map { genre -> genre.name }
            .toImmutableList(),
        posterUrl = MovieSample.Inception.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = MovieSample.Inception.rating.average.value.toString(),
        releaseYear = MovieSample.Inception.releaseDate.orNull()?.year.toString(),
        suggestionSource = TextRes(string.suggestions_source_suggested),
        title = MovieSample.Inception.title,
        tmdbScreenplayId = MovieSample.Inception.tmdbId
    )

    val TheWolfOfWallStreet = ForYouScreenplayUiModel(
        actors = MovieCreditsSample.TheWolfOfWallStreet.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouScreenplayUiModel.Actor(imageUrl.orNull().orEmpty())
        }.toImmutableList(),
        affinity = SuggestedMovieSample.TheWolfOfWallStreet.affinity.value,
        backdropUrl = MovieSample.TheWolfOfWallStreet.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        genres = MovieWithExtrasSample.TheWolfOfWallStreet.movieWithDetails.genres.map { genre -> genre.name }
            .toImmutableList(),
        posterUrl = MovieSample.TheWolfOfWallStreet.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = MovieSample.TheWolfOfWallStreet.rating.average.value.toString(),
        releaseYear = MovieSample.TheWolfOfWallStreet.releaseDate.orNull()?.year.toString(),
        suggestionSource = TextRes(string.suggestions_source_popular),
        title = MovieSample.TheWolfOfWallStreet.title,
        tmdbScreenplayId = MovieSample.TheWolfOfWallStreet.tmdbId
    )

    val War = ForYouScreenplayUiModel(
        actors = MovieCreditsSample.War.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouScreenplayUiModel.Actor(imageUrl.orNull().orEmpty())
        }.toImmutableList(),
        affinity = SuggestedMovieSample.War.affinity.value,
        backdropUrl = MovieSample.War.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        genres = MovieWithExtrasSample.War.movieWithDetails.genres.map { genre -> genre.name }
            .toImmutableList(),
        posterUrl = MovieSample.War.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = MovieSample.War.rating.average.value.toString(),
        releaseYear = MovieSample.War.releaseDate.orNull()?.year.toString(),
        suggestionSource = TextRes(string.suggestions_source_popular),
        title = MovieSample.War.title,
        tmdbScreenplayId = MovieSample.War.tmdbId
    )
}
