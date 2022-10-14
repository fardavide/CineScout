package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseTmdbTvShowId

val TmdbTvShowIdAdapter = object : ColumnAdapter<DatabaseTmdbTvShowId, Long> {

    override fun decode(databaseValue: Long) = DatabaseTmdbTvShowId(databaseValue.toInt())

    override fun encode(value: DatabaseTmdbTvShowId) = value.value.toLong()
}
