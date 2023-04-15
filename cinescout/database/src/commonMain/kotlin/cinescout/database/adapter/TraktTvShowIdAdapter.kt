package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseScreenplayId.TypeTvShow
import cinescout.database.model.DatabaseScreenplayId.ValueSeparator
import cinescout.database.model.DatabaseTraktTvShowId

val TraktTvShowIdAdapter = object : ColumnAdapter<DatabaseTraktTvShowId, String> {

    override fun decode(databaseValue: String): DatabaseTraktTvShowId {
        val (type, id) = databaseValue.split(ValueSeparator)
        return when (type) {
            TypeTvShow -> DatabaseTraktTvShowId(id.toInt())
            else -> error("Unknown type: $type")
        }
    }

    override fun encode(value: DatabaseTraktTvShowId) = "$TypeTvShow$ValueSeparator${value.value}"
}
