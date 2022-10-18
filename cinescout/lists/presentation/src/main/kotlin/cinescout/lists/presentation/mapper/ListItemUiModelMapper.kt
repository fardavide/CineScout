package cinescout.lists.presentation.mapper

import cinescout.common.model.TmdbPosterImage
import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowWithPersonalRating

class ListItemUiModelMapper {

    fun toUiModel(movie: Movie) = ListItemUiModel.Movie(
        personalRating = null,
        posterUrl = movie.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = movie.rating.average.value.toString(),
        title = movie.title,
        tmdbId = movie.tmdbId
    )

    fun toUiModel(movieWithPersonalRating: MovieWithPersonalRating) = ListItemUiModel.Movie(
        personalRating = movieWithPersonalRating.personalRating.value.toString(),
        posterUrl = movieWithPersonalRating.movie.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = movieWithPersonalRating.movie.rating.average.value.toString(),
        title = movieWithPersonalRating.movie.title,
        tmdbId = movieWithPersonalRating.movie.tmdbId
    )

    fun toUiModel(tvShow: TvShow) = ListItemUiModel.TvShow(
        personalRating = null,
        posterUrl = tvShow.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = tvShow.rating.average.value.toString(),
        title = tvShow.title,
        tmdbId = tvShow.tmdbId
    )

    fun toUiModel(tvShowWithPersonalRating: TvShowWithPersonalRating) = ListItemUiModel.TvShow(
        personalRating = tvShowWithPersonalRating.personalRating.value.toString(),
        posterUrl = tvShowWithPersonalRating.tvShow.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = tvShowWithPersonalRating.tvShow.rating.average.value.toString(),
        title = tvShowWithPersonalRating.tvShow.title,
        tmdbId = tvShowWithPersonalRating.tvShow.tmdbId
    )
}
