package cinescout.auth.tmdb.data.local.mapper

import cinescout.auth.tmdb.data.model.TmdbAccessToken
import cinescout.database.model.DatabaseTmdbAccessToken

fun TmdbAccessToken.toDatabaseAccessToken() = DatabaseTmdbAccessToken(value)
