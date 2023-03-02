package cinescout.suggestions.presentation.mapper

import cinescout.movies.domain.model.MovieWithExtras
import cinescout.screenplay.domain.model.CastMember
import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage
import cinescout.screenplay.domain.model.TmdbProfileImage
import cinescout.suggestions.presentation.model.ForYouScreenplayUiModel
import cinescout.tvshows.domain.model.TvShowWithExtras
import kotlinx.collections.immutable.toImmutableList
import org.koin.core.annotation.Factory

@Factory
class ForYouItemUiModelMapper {

    fun toUiModel(movieWithExtras: MovieWithExtras): ForYouScreenplayUiModel {
        val credits = movieWithExtras.credits
        val movie = movieWithExtras.movieWithDetails.movie
        return ForYouScreenplayUiModel(
            actors = toMovieActorsUiModels(credits.cast).toImmutableList(),
            backdropUrl = movie.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
            genres = movieWithExtras.movieWithDetails.genres.map { genre -> genre.name }.toImmutableList(),
            posterUrl = movie.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
            rating = movie.rating.average.value.toString(),
            releaseYear = movie.releaseDate.orNull()?.year?.toString().orEmpty(),
            title = movie.title,
            tmdbScreenplayId = movie.tmdbId
        )
    }

    fun toUiModel(tvShowWithExtras: TvShowWithExtras): ForYouScreenplayUiModel {
        val credits = tvShowWithExtras.credits
        val tvShow = tvShowWithExtras.tvShowWithDetails.tvShow
        return ForYouScreenplayUiModel(
            actors = toTvShowActorsUiModels(credits.cast).toImmutableList(),
            backdropUrl = tvShow.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
            genres = tvShowWithExtras.tvShowWithDetails.genres.map { genre -> genre.name }.toImmutableList(),
            posterUrl = tvShow.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
            rating = tvShow.rating.average.value.toString(),
            releaseYear = tvShow.firstAirDate.year.toString(),
            title = tvShow.title,
            tmdbScreenplayId = tvShow.tmdbId
        )
    }

    private fun toMovieActorsUiModels(actors: List<CastMember>): List<ForYouScreenplayUiModel.Actor> =
        actors.map { member ->
            val profileImageUrl = member.person.profileImage
                .map { image -> image.getUrl(TmdbProfileImage.Size.SMALL) }
                .orNull()
            ForYouScreenplayUiModel.Actor(profileImageUrl = profileImageUrl)
        }

    private fun toTvShowActorsUiModels(actors: List<CastMember>): List<ForYouScreenplayUiModel.Actor> =
        actors.map { member ->
            val profileImageUrl = member.person.profileImage
                .map { image -> image.getUrl(TmdbProfileImage.Size.SMALL) }
                .orNull()
            ForYouScreenplayUiModel.Actor(profileImageUrl = profileImageUrl)
        }
}
