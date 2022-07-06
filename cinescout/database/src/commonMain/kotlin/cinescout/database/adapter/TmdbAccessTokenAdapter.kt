package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseTmdbAccessToken

val TmdbAccessTokenAdapter = object : ColumnAdapter<DatabaseTmdbAccessToken, String> {
    override fun decode(databaseValue: String) = DatabaseTmdbAccessToken(databaseValue)

    override fun encode(value: DatabaseTmdbAccessToken) = value.value
}
