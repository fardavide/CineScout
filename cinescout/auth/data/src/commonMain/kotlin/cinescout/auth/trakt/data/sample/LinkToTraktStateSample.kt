package cinescout.auth.trakt.data.sample

import cinescout.auth.domain.usecase.LinkToTrakt

object LinkToTraktStateSample {

    const val AppAuthenticationUrl = "https://trakt.tv/oauth/authorize"

    val Success = LinkToTrakt.State.Success
}
