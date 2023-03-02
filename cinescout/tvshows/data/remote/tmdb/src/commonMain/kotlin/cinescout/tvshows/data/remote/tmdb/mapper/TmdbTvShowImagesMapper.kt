package cinescout.tvshows.data.remote.tmdb.mapper

import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage
import cinescout.tvshows.data.remote.tmdb.model.GetTvShowImages
import cinescout.tvshows.domain.model.TvShowImages
import org.koin.core.annotation.Factory

@Factory
internal class TmdbTvShowImagesMapper {

    fun toTvShowImages(images: GetTvShowImages.Response) = TvShowImages(
        backdrops = images.backdrops.map(::toBackdrop),
        tvShowId = images.tvShowId,
        posters = images.posters.map(::toPoster)
    )

    private fun toBackdrop(poster: GetTvShowImages.Response.Backdrop) = TmdbBackdropImage(path = poster.path)

    private fun toPoster(poster: GetTvShowImages.Response.Poster) = TmdbPosterImage(path = poster.path)
}
