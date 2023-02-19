package cinescout.auth.tmdb.domain.sample

import cinescout.auth.tmdb.domain.usecase.LinkToTmdb

object LinkToTmdbStateSample {

    private const val TokenAuthenticationUrl = "https://www.themoviedb.org/authenticate/"

    val Success = LinkToTmdb.State.Success

    val UserShouldAuthorizeToken =
        LinkToTmdb.State.UserShouldAuthorizeToken("${TokenAuthenticationUrl}Request token")
}
