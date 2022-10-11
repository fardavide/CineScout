package cinescout.movies.data.remote.tmdb.mapper

import cinescout.common.model.TmdbBackdropImage
import cinescout.common.model.TmdbPosterImage
import cinescout.movies.data.remote.tmdb.model.GetMovieImages
import cinescout.movies.domain.model.MovieImages

class TmdbMovieImagesMapper {

    fun toMovieImages(images: GetMovieImages.Response) = MovieImages(
        backdrops = images.backdrops.map(::toBackdrop),
        movieId = images.movieId,
        posters = images.posters.map(::toPoster)
    )

    private fun toBackdrop(poster: GetMovieImages.Response.Backdrop) =
        TmdbBackdropImage(path = poster.path)

    private fun toPoster(poster: GetMovieImages.Response.Poster) =
        TmdbPosterImage(path = poster.path)
}
