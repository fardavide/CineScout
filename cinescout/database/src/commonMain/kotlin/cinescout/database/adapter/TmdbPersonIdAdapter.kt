package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseTmdbPersonId

val TmdbPersonIdAdapter = object : ColumnAdapter<DatabaseTmdbPersonId, Long> {

    override fun decode(databaseValue: Long) = DatabaseTmdbPersonId(databaseValue.toInt())

    override fun encode(value: DatabaseTmdbPersonId) = value.value.toLong()
}
