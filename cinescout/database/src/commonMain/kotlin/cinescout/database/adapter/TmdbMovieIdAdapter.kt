package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseTmdbMovieId

val TmdbMovieIdAdapter = object : ColumnAdapter<DatabaseTmdbMovieId, Long> {

    override fun decode(databaseValue: Long) = DatabaseTmdbMovieId(databaseValue.toInt())

    override fun encode(value: DatabaseTmdbMovieId) = value.value.toLong()
}
