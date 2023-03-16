package cinescout.media.data.remote.mapper

import cinescout.media.data.remote.model.GetScreenplayImagesResponse
import cinescout.media.data.remote.model.GetScreenplayImagesResponseWithId
import cinescout.media.domain.model.ScreenplayImages
import cinescout.media.domain.model.TmdbBackdropImage
import cinescout.media.domain.model.TmdbPosterImage
import org.koin.core.annotation.Factory

@Factory
internal class TmdbScreenplayImagesMapper {

    fun toScreenplayImages(images: GetScreenplayImagesResponseWithId) = ScreenplayImages(
        backdrops = images.response.backdrops.map(::toBackdrop),
        posters = images.response.posters.map(::toPoster),
        screenplayId = images.screenplayId
    )

    private fun toBackdrop(poster: GetScreenplayImagesResponse.Backdrop) =
        TmdbBackdropImage(path = poster.path)

    private fun toPoster(poster: GetScreenplayImagesResponse.Poster) = TmdbPosterImage(path = poster.path)
}
