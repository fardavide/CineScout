package cinescout.media.presentation

import arrow.core.getOrElse
import cinescout.media.domain.model.MediaRequest
import cinescout.media.domain.model.TmdbBackdropImage
import cinescout.media.domain.model.TmdbPosterImage
import cinescout.media.domain.store.ScreenplayImagesStore
import coil.intercept.Interceptor
import coil.request.ImageRequest
import coil.request.ImageResult
import org.koin.core.annotation.Factory

@Factory
class CoilMediaRequestInterceptor(
    private val imagesStore: ScreenplayImagesStore
) : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = when (val data = chain.request.data) {
            is MediaRequest -> handle(chain, data)
            else -> chain.request
        }
        return chain.proceed(request)
    }

    private suspend fun handle(chain: Interceptor.Chain, mediaRequest: MediaRequest): ImageRequest {
        val images = imagesStore.getCached(mediaRequest.screenplayId, refresh = false)
            .getOrElse { return chain.request.newBuilder().data(it).build() }

        val url = when (mediaRequest) {
            is MediaRequest.Backdrop -> images.primaryBackdrop()?.getUrl(TmdbBackdropImage.Size.ORIGINAL)
            is MediaRequest.Poster -> images.primaryPoster()?.getUrl(TmdbPosterImage.Size.ORIGINAL)
        } ?: return chain.request

        return chain.request.newBuilder()
            .data(url)
            .build()
    }
}
