package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.id.DatabaseTmdbEpisodeId

val TmdbEpisodeIdAdapter = object : ColumnAdapter<DatabaseTmdbEpisodeId, Long> {

    override fun decode(databaseValue: Long) = DatabaseTmdbEpisodeId(value = databaseValue.toInt())

    override fun encode(value: DatabaseTmdbEpisodeId) = value.value.toLong()
}
