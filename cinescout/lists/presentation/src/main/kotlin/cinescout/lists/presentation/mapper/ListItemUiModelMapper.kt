package cinescout.lists.presentation.mapper

import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.TmdbPosterImage

class ListItemUiModelMapper {

    fun toUiModel(movie: Movie) = ListItemUiModel(
        personalRating = null,
        posterUrl = movie.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = movie.rating.average.value.toString(),
        title = movie.title,
        tmdbId = movie.tmdbId
    )

    fun toUiModel(movieWithPersonalRating: MovieWithPersonalRating) = ListItemUiModel(
        personalRating = movieWithPersonalRating.personalRating.value.toString(),
        posterUrl = movieWithPersonalRating.movie.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = movieWithPersonalRating.movie.rating.average.value.toString(),
        title = movieWithPersonalRating.movie.title,
        tmdbId = movieWithPersonalRating.movie.tmdbId
    )
}
