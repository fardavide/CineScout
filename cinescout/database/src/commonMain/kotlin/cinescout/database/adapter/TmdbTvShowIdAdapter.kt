package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseTmdbScreenplayId.Companion.TypeTvShow
import cinescout.database.model.DatabaseTmdbScreenplayId.Companion.ValueSeparator
import cinescout.database.model.DatabaseTmdbTvShowId

val TmdbTvShowIdAdapter = object : ColumnAdapter<DatabaseTmdbTvShowId, String> {

    override fun decode(databaseValue: String): DatabaseTmdbTvShowId {
        val (type, id) = databaseValue.split(ValueSeparator)
        return when (type) {
            TypeTvShow -> DatabaseTmdbTvShowId(id.toInt())
            else -> error("Unknown type: $type")
        }
    }

    override fun encode(value: DatabaseTmdbTvShowId) = "$TypeTvShow$ValueSeparator${value.value}"
}
