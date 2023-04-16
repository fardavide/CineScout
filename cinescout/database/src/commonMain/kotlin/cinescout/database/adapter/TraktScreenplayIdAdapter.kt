package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseScreenplayId.TypeMovie
import cinescout.database.model.DatabaseScreenplayId.TypeTvShow
import cinescout.database.model.DatabaseScreenplayId.ValueSeparator
import cinescout.database.model.DatabaseTraktMovieId
import cinescout.database.model.DatabaseTraktScreenplayId
import cinescout.database.model.DatabaseTraktTvShowId

val TraktScreenplayIdAdapter = object : ColumnAdapter<DatabaseTraktScreenplayId, String> {

    override fun decode(databaseValue: String): DatabaseTraktScreenplayId {
        val (type, id) = databaseValue.split(ValueSeparator)
        return when (type) {
            TypeMovie -> DatabaseTraktMovieId(id.toInt())
            TypeTvShow -> DatabaseTraktTvShowId(id.toInt())
            else -> error("Unknown type: $type")
        }
    }

    override fun encode(value: DatabaseTraktScreenplayId) = when (value) {
        is DatabaseTraktMovieId -> "$TypeMovie$ValueSeparator${value.value}"
        is DatabaseTraktTvShowId -> "$TypeTvShow$ValueSeparator${value.value}"
    }
}
