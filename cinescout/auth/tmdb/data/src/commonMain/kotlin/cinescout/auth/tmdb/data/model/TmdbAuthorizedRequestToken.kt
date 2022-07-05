package cinescout.auth.tmdb.data.model

@JvmInline
value class TmdbAuthorizedRequestToken internal constructor(val value: String)

fun Authorized(requestToken: String) = TmdbAuthorizedRequestToken(requestToken)
fun Authorized(requestToken: TmdbRequestToken) = TmdbAuthorizedRequestToken(requestToken.value)
