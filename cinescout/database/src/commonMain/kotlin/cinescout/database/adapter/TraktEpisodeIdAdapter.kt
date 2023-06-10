package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.id.DatabaseTraktEpisodeId

val TraktEpisodeIdAdapter = object : ColumnAdapter<DatabaseTraktEpisodeId, Long> {

    override fun decode(databaseValue: Long) = DatabaseTraktEpisodeId(value = databaseValue.toInt())

    override fun encode(value: DatabaseTraktEpisodeId) = value.value.toLong()
}
