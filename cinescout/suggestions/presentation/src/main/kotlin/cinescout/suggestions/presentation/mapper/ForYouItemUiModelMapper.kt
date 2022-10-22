package cinescout.suggestions.presentation.mapper

import cinescout.common.model.CastMember
import cinescout.common.model.TmdbBackdropImage
import cinescout.common.model.TmdbPosterImage
import cinescout.common.model.TmdbProfileImage
import cinescout.movies.domain.model.MovieWithExtras
import cinescout.suggestions.presentation.model.ForYouMovieUiModel
import cinescout.suggestions.presentation.model.ForYouTvShowUiModel
import cinescout.tvshows.domain.model.TvShowWithExtras

class ForYouItemUiModelMapper {

    fun toUiModel(movieWithExtras: MovieWithExtras): ForYouMovieUiModel {
        val credits = movieWithExtras.credits
        val movie = movieWithExtras.movieWithDetails.movie
        return ForYouMovieUiModel(
            actors = toMovieActorsUiModels(credits.cast),
            backdropUrl = movie.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
            genres = movieWithExtras.movieWithDetails.genres.map { genre -> genre.name },
            posterUrl = movie.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
            rating = movie.rating.average.value.toString(),
            releaseYear = movie.releaseDate.orNull()?.year?.toString().orEmpty(),
            title = movie.title,
            tmdbMovieId = movie.tmdbId
        )
    }

    fun toUiModel(tvShowWithExtras: TvShowWithExtras): ForYouTvShowUiModel {
        val credits = tvShowWithExtras.credits
        val tvShow = tvShowWithExtras.tvShowWithDetails.tvShow
        return ForYouTvShowUiModel(
            actors = toTvShowActorsUiModels(credits.cast),
            backdropUrl = tvShow.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
            firstAirDate = tvShow.firstAirDate.year?.toString().orEmpty(),
            genres = tvShowWithExtras.tvShowWithDetails.genres.map { genre -> genre.name },
            posterUrl = tvShow.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
            rating = tvShow.rating.average.value.toString(),
            title = tvShow.title,
            tmdbTvShowId = tvShow.tmdbId
        )
    }

    private fun toMovieActorsUiModels(actors: List<CastMember>): List<ForYouMovieUiModel.Actor> =
        actors.map { member ->
            val profileImageUrl = member.person.profileImage
                .map { image -> image.getUrl(TmdbProfileImage.Size.SMALL) }
                .orNull()
            ForYouMovieUiModel.Actor(profileImageUrl = profileImageUrl)
        }

    private fun toTvShowActorsUiModels(actors: List<CastMember>): List<ForYouTvShowUiModel.Actor> =
        actors.map { member ->
            val profileImageUrl = member.person.profileImage
                .map { image -> image.getUrl(TmdbProfileImage.Size.SMALL) }
                .orNull()
            ForYouTvShowUiModel.Actor(profileImageUrl = profileImageUrl)
        }
}
