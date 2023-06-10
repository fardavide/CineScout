package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.id.DatabaseTmdbKeywordId

val TmdbKeywordIdAdapter = object : ColumnAdapter<DatabaseTmdbKeywordId, Long> {

    override fun decode(databaseValue: Long) = DatabaseTmdbKeywordId(databaseValue.toInt())

    override fun encode(value: DatabaseTmdbKeywordId) = value.value.toLong()
}
