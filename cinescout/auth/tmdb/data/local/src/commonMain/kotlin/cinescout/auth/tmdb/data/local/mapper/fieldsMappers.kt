package cinescout.auth.tmdb.data.local.mapper

import cinescout.auth.tmdb.data.model.TmdbAccessToken
import cinescout.auth.tmdb.data.model.TmdbAccountId
import cinescout.auth.tmdb.data.model.TmdbSessionId
import cinescout.database.model.DatabaseTmdbAccessToken
import cinescout.database.model.DatabaseTmdbAccountId
import cinescout.database.model.DatabaseTmdbSessionId

fun TmdbAccessToken.toDatabaseAccessToken() = DatabaseTmdbAccessToken(value)
fun TmdbAccountId.toDatabaseAccountId() = DatabaseTmdbAccountId(value)
fun TmdbSessionId.toDatabaseSessionId() = DatabaseTmdbSessionId(value)
