package cinescout.media.domain.model

import cinescout.screenplay.domain.model.TmdbScreenplayId

sealed interface MediaRequest {

    val screenplayId: TmdbScreenplayId

    data class Backdrop(override val screenplayId: TmdbScreenplayId) : MediaRequest

    data class Poster(override val screenplayId: TmdbScreenplayId) : MediaRequest
}

fun TmdbScreenplayId.asBackdropRequest() = MediaRequest.Backdrop(this)
fun TmdbScreenplayId.asPosterRequest() = MediaRequest.Poster(this)

