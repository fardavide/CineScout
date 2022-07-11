package cinescout.auth.tmdb.data.local.mapper

import cinescout.auth.tmdb.data.model.TmdbAccessToken
import cinescout.auth.tmdb.data.model.TmdbAccountId
import cinescout.auth.tmdb.data.model.TmdbCredentials
import cinescout.auth.tmdb.data.model.TmdbSessionId
import cinescout.database.model.DatabaseTmdbAccessToken
import cinescout.database.model.DatabaseTmdbAccountId
import cinescout.database.model.DatabaseTmdbCredentials
import cinescout.database.model.DatabaseTmdbSessionId

fun DatabaseTmdbAccountId.toAccountId() = TmdbAccountId(value)
fun DatabaseTmdbAccessToken.toAccessToken() = TmdbAccessToken(value)
fun DatabaseTmdbCredentials.toCredentials() = TmdbCredentials(
    accountId = accountId.toAccountId(),
    accessToken = accessToken.toAccessToken(),
    sessionId = sessionId.toSessionId()
)
fun DatabaseTmdbSessionId.toSessionId() = TmdbSessionId(value)
fun TmdbAccessToken.toDatabaseAccessToken() = DatabaseTmdbAccessToken(value)
fun TmdbAccountId.toDatabaseAccountId() = DatabaseTmdbAccountId(value)
fun TmdbCredentials.toDatabaseCredentials() = DatabaseTmdbCredentials(
    id = 0,
    accountId = accountId.toDatabaseAccountId(),
    accessToken = accessToken.toDatabaseAccessToken(),
    sessionId = sessionId.toDatabaseSessionId()
)
fun TmdbSessionId.toDatabaseSessionId() = DatabaseTmdbSessionId(value)
