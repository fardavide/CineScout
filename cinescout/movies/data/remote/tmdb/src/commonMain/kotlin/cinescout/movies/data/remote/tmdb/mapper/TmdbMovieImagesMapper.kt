package cinescout.movies.data.remote.tmdb.mapper

import cinescout.movies.data.remote.tmdb.model.GetMovieImages
import cinescout.screenplay.domain.model.MovieImages
import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage
import org.koin.core.annotation.Factory

@Factory
internal class TmdbMovieImagesMapper {

    fun toMovieImages(images: GetMovieImages.Response) = MovieImages(
        backdrops = images.backdrops.map(::toBackdrop),
        screenplayId = images.movieId,
        posters = images.posters.map(::toPoster)
    )

    private fun toBackdrop(poster: GetMovieImages.Response.Backdrop) = TmdbBackdropImage(path = poster.path)

    private fun toPoster(poster: GetMovieImages.Response.Poster) = TmdbPosterImage(path = poster.path)
}
