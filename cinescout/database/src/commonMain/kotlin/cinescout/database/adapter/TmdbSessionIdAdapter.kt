package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseTmdbAccessToken
import cinescout.database.model.DatabaseTmdbSessionId

val TmdbSessionIdAdapter = object : ColumnAdapter<DatabaseTmdbSessionId, String> {
    override fun decode(databaseValue: String) = DatabaseTmdbSessionId(databaseValue)

    override fun encode(value: DatabaseTmdbSessionId) = value.value
}
