package cinescout.media.domain.model

import cinescout.screenplay.domain.model.TmdbScreenplayId

sealed interface ImageRequest {

    val screenplayId: TmdbScreenplayId

    data class Backdrop(override val screenplayId: TmdbScreenplayId) : ImageRequest

    data class Poster(override val screenplayId: TmdbScreenplayId) : ImageRequest
}

fun TmdbScreenplayId.asBackdropRequest() = ImageRequest.Backdrop(this)
fun TmdbScreenplayId.asPosterRequest() = ImageRequest.Poster(this)

