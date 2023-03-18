package cinescout.media.data.remote.mapper

import cinescout.media.data.remote.model.GetScreenplayImagesResponseWithId
import cinescout.media.data.remote.model.ScreenplayImagesBody
import cinescout.media.domain.model.ScreenplayImages
import cinescout.media.domain.model.TmdbBackdropImage
import cinescout.media.domain.model.TmdbPosterImage
import org.koin.core.annotation.Factory

@Factory
internal class TmdbScreenplayImagesMapper {

    fun toScreenplayImages(images: GetScreenplayImagesResponseWithId) = ScreenplayImages(
        backdrops = images.response.images.backdrops.map { backdrop ->
            toBackdrop(primaryPath = images.response.backdropPath, backdrop)
        },
        posters = images.response.images.posters.map { poster ->
            toPoster(primaryPath = images.response.posterPath, poster)
        },
        screenplayId = images.screenplayId
    )

    private fun toBackdrop(primaryPath: String?, backdrop: ScreenplayImagesBody.Backdrop) =
        TmdbBackdropImage(isPrimary = backdrop.path == primaryPath, path = backdrop.path)

    private fun toPoster(primaryPath: String?, poster: ScreenplayImagesBody.Poster) =
        TmdbPosterImage(isPrimary = poster.path == primaryPath, path = poster.path)
}
