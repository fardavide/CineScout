package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseTmdbVideoId

val TmdbVideoIdAdapter = object : ColumnAdapter<DatabaseTmdbVideoId, String> {

    override fun decode(databaseValue: String) = DatabaseTmdbVideoId(databaseValue)

    override fun encode(value: DatabaseTmdbVideoId) = value.value
}
