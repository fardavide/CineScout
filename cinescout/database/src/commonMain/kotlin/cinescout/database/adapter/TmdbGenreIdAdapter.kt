package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.id.DatabaseTmdbGenreId

val TmdbGenreIdAdapter = object : ColumnAdapter<DatabaseTmdbGenreId, Long> {

    override fun decode(databaseValue: Long) = DatabaseTmdbGenreId(databaseValue.toInt())

    override fun encode(value: DatabaseTmdbGenreId) = value.value.toLong()
}
